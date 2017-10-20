package de.douglas.springboot.filter;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.google.common.collect.Sets;

public class DouglasRewriteFilter implements Filter {

  private final Logger FILTER_LOG = Logger.getLogger(DouglasRewriteFilter.class);

  /**
   * set of HTTP methods that will get a redirect instead of silent internal handling
   */
  private static final Set<String> REDIRECT_METHODS = Sets.newHashSet("GET", "HEAD");

  /**
   * path prefix to be removed from request
   */
  private static final String PATH_DOUGLAS = "/douglas/";
  /**
   * path prefix to be removed from request
   */
  private static final String PATH_DOUGLAS_PLAY = "/douglas/play/";
  /**
   * replacement for stripped paths
   */
  private static final String PATH_SINGLE_SLASH = "/";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    FILTER_LOG.trace("Filter init()");
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    FILTER_LOG.trace("Filter doFilter()");

    if (servletRequest instanceof RewriteRequestWrapper) {
      FILTER_LOG.trace("Request is already of type RewriteRequestWrapper and was therefore processed.");
      filterChain.doFilter(servletRequest, servletResponse);
      return; // dont process any more
    }

    if (servletRequest instanceof HttpServletRequest) {
      FILTER_LOG.trace("Request is HttpServletRequest");
      final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
      final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
      final String method = httpRequest.getMethod();
      final String requestURI = httpRequest.getRequestURI();
      FILTER_LOG.debug("Original Request: '" + method + " " + requestURI + "'");

      final String removePrefix;
      if (requestURI.startsWith(PATH_DOUGLAS_PLAY)) {
        FILTER_LOG.debug("I say the RequestURI starts with: '" + PATH_DOUGLAS_PLAY + "'");
        removePrefix = PATH_DOUGLAS_PLAY;
      } else if (requestURI.startsWith(PATH_DOUGLAS)) {
        FILTER_LOG.debug("I say the RequestURI starts with: '" + PATH_DOUGLAS + "'");
        removePrefix = PATH_DOUGLAS;
      } else {
        removePrefix = null;
      }

      if (removePrefix != null) {
        // something needs to be redirected
        final RewriteRequestWrapper rewriteRequest = new RewriteRequestWrapper(removePrefix, httpRequest);
        FILTER_LOG.trace("Request was wrapped.");
        final String newRequestUrl = rewriteRequest.getRequestURL().toString();

        if (REDIRECT_METHODS.contains(method.toUpperCase(Locale.ENGLISH))) {
          // send a HTTP redirect to the client
          httpResponse.reset();
          httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
          httpResponse.setHeader("Location", newRequestUrl);
          FILTER_LOG.warn("Redirecting client to '" + newRequestUrl + "'");
          return; // skip any other processing
        } else {
          // redirect request internally
          final String newRequestUri = rewriteRequest.getRequestURI();
          FILTER_LOG.debug("Looking up dispatcher for '" + newRequestUri + "'");
          final RequestDispatcher newDispatcher = servletRequest.getRequestDispatcher(newRequestUri);
          if (newDispatcher != null) {
            FILTER_LOG.warn("Forwarding request from '" + requestURI + "' to new dispatcher for '" + newRequestUri + "'");
            newDispatcher.forward(rewriteRequest, servletResponse);
          } else {
            FILTER_LOG.warn("Could not find a dispatcher for new URI '" + newRequestUri + "'");
            throw new ServletException("Could not find new destination for rewritten URI");
          }
        }
      } else { 
        // no rewrite necessary. proceed as normal
        filterChain.doFilter(servletRequest, servletResponse);
      }
    }
  }

  static class RewriteRequestWrapper extends HttpServletRequestWrapper {
    private final Logger WRAPPER_LOG = Logger.getLogger(RewriteRequestWrapper.class);

    private final String removePath;

    RewriteRequestWrapper(final String removePath, HttpServletRequest request) {
      super(request);
      if (WRAPPER_LOG.isEnabledFor(Priority.WARN)) {
        final String requestUrl = request.getRequestURL().toString();
        final String method = request.getMethod();
        WRAPPER_LOG.warn("Creating Request-Wrapper to remove path component '" + removePath + "' from '" + method + " " + requestUrl + "'");
      }
      this.removePath = removePath;
    }

    @Override
    public String getPathInfo() {
      final String pathInfo = super.getPathInfo();
      if (pathInfo != null) {
        final String replaced = pathInfo.replaceFirst(removePath, PATH_SINGLE_SLASH);
        WRAPPER_LOG.trace("Reduced getPathInfo() from '" + pathInfo + "' to '" + replaced + "'");
        return replaced;
      } else {
        return null;
      }
    }

    @Override
    public String getPathTranslated() {
      final String pathTranslated = super.getPathTranslated();
      if (pathTranslated != null) {
        final String replaced = pathTranslated.replaceFirst(removePath, PATH_SINGLE_SLASH);
        WRAPPER_LOG.trace("Reduced getPathTranslated() from '" + pathTranslated + "' to '" + replaced + "'");
        return replaced;
      } else {
        return null;
      }
    }

    @Override
    public String getRequestURI() {
      final String requestURI = super.getRequestURI();
      if (requestURI != null) {
        final String replaced = requestURI.replaceFirst(removePath, PATH_SINGLE_SLASH);
        WRAPPER_LOG.trace("Reduced getRequestURI() from '" + requestURI + "' to '" + replaced + "'");
        return replaced;
      } else {
        return null;
      }
    }

    @Override
    public StringBuffer getRequestURL() {
      final StringBuffer requestURL = super.getRequestURL();
      if (requestURL != null) {
        final StringBuffer replaced = new StringBuffer(requestURL.toString().replaceFirst(removePath, PATH_SINGLE_SLASH));
        WRAPPER_LOG.trace("Reduced getRequestURL() from '" + requestURL + "' to '" + replaced + "'");
        return replaced;
      } else {
        return null;
      }
    }

  }

  @Override
  public void destroy() {
    FILTER_LOG.trace("Filter destroy()");
  }
}