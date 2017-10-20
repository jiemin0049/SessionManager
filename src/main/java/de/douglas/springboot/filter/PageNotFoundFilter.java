package de.douglas.springboot.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class PageNotFoundFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    
      PageNotFoundResponse notfound = new PageNotFoundResponse((HttpServletResponse) response);
      chain.doFilter(request, notfound);
      int status = notfound.getStatus();
      if (status == HttpServletResponse.SC_NOT_FOUND) {
        notfound.setStatus(HttpServletResponse.SC_NOT_FOUND);
        //notfound.setHeader("Location", "http://www.google.de");
        notfound.sendRedirect("www.google.de");
      }
  }

  static class PageNotFoundResponse extends HttpServletResponseWrapper {

    private int httpStatus;

    public PageNotFoundResponse(HttpServletResponse response) {
      super(response);
    }

    @Override
    public void setStatus(int sc) {
      httpStatus = sc;
      super.setStatus(sc);
    }

    public int getStatus() {
      return httpStatus;
    }

    @Override
    public void setStatus(int status, String string) {
      super.setStatus(status, string);
      this.httpStatus = status;
    }

    @Override
    public void reset() {
      super.reset();
      this.httpStatus = SC_OK;
    }

    @Override
    public void sendRedirect(String location) throws IOException {
      httpStatus = SC_MOVED_TEMPORARILY;
      super.sendRedirect(location);
    }

    @Override
    public void sendError(int sc) throws IOException {
      httpStatus = sc;
      super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
      httpStatus = sc;
      super.sendError(sc, msg);
    }

  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void destroy() {

  }
}