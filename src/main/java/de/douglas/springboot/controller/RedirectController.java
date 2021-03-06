package de.douglas.springboot.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import de.douglas.springboot.service.SessionService;

/**
 * If a request e.g. /douglas/xxxxx/xxxx/xxx.. or /douglas/abc.html?id=1&name=username&... 
 * The prefix /douglas/ must be cut off, and the cutting link will be redirected to other controller.
 * 
 * This redirect class can ONLY be used for request method GET.
 */
@Controller
public class RedirectController {

  @Autowired
  SessionService sessionService;

  /**
   * Using response to redirect,here redirect response is HTTP status code 302.
   * 
   * The parameters can not send automatically, must send with link?key=value&key=value...
   * 
   * http://localhost:8080/SessionManager/douglas0/hello
   *
   * @param request
   */
  @RequestMapping(value = "/douglas0/**", method = RequestMethod.GET)
  public void redirectRequest0(HttpServletRequest request, HttpServletResponse response) {
    String servletPath = request.getServletPath();
    AntPathMatcher apm = new AntPathMatcher();
    // Get RequestMapping value string, here get "/douglas/**"
    String matchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    // Cut off prefix "/douglas/**"
    StringBuilder finalPath = new StringBuilder(apm.extractPathWithinPattern(matchPattern, servletPath));
    Map<String, String[]> parameterMap = request.getParameterMap();
    String and = "";
    if (!parameterMap.isEmpty()) {
      finalPath.append("?");
      for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
        finalPath.append(and).append(entry.getKey()).append("=").append(entry.getValue()[0]);
        and = "&";
      }
    }
    try {
      response.sendRedirect(request.getContextPath() + "/" + finalPath);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Using Set Header to redirect,here edirect response is HTTP status code 301.
   * 
   * BUT the parameters can not send automatically, must send with link?key=value&key=value...
   *
   * @param request
   */
  @RequestMapping(value = "/douglas1/**", method = RequestMethod.GET)
  public void redirectRequest1(HttpServletRequest request, HttpServletResponse response) {
    String servletPath = request.getServletPath();
    AntPathMatcher apm = new AntPathMatcher();
    // Get RequestMapping value string, here get "/douglas/**"
    String matchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    // Cut off prefix "/douglas/**"
    StringBuilder finalPath = new StringBuilder(apm.extractPathWithinPattern(matchPattern, servletPath));
    Map<String, String[]> parameterMap = request.getParameterMap();
    String and = "";
    if (!parameterMap.isEmpty()) {
      finalPath.append("?");
      for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
        finalPath.append(and).append(entry.getKey()).append("=").append(entry.getValue()[0]);
        and = "&";
      }
    }

    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    response.setHeader("Location", request.getContextPath() + "/" + finalPath);
  }

  /**
   * Using RedirectView to redirect. Here redirect response (HTTP status code 301, "Moved Permanently").
   *
   * @param request
   */
  @RequestMapping(value = "/douglas2/**", method = RequestMethod.GET)
  public ModelAndView redirectRequest2(HttpServletRequest request) {
    String servletPath = request.getServletPath();
    AntPathMatcher apm = new AntPathMatcher();
    // Get RequestMapping value string, here retrieves "/douglas/**"
    String matchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    // Cut off prefix "/douglas/"
    String finalPath = apm.extractPathWithinPattern(matchPattern, servletPath);
    RedirectView redirectView = new RedirectView("/" + finalPath, true);
    redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
    redirectView.setAttributesMap(request.getParameterMap());
    return new ModelAndView(redirectView);
  }

  /**
   * Server side redirect..
   * 
   * Parameters can be send automatically.
   *
   * @param request
   */
  @RequestMapping(value = "/douglas3/**", method = RequestMethod.GET)
  public void redirectRequest3(HttpServletRequest request, HttpServletResponse response) {
    String servletPath = request.getServletPath();
    AntPathMatcher apm = new AntPathMatcher();
    // Get RequestMapping value string, here get "/douglas/**"
    String matchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    // Cut off prefix "/douglas/**"
    String finalPath = apm.extractPathWithinPattern(matchPattern, servletPath);
    try {
      request.getRequestDispatcher(finalPath).forward(request, response);
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (ServletException e) {
      e.printStackTrace();
    }
  }

  /**
   * Using request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY)
   * set redirect status code 301.
   * 
   * Since Spring 4.0 ?
   * 
   * http://localhost:8080/SessionManager/douglas4/hello
   * 
   */
  @RequestMapping(value = "/douglas4/**", method = RequestMethod.GET)
  public String redirectRequest4(HttpServletRequest request) {
    String servletPath = request.getServletPath();
    AntPathMatcher apm = new AntPathMatcher();
    // Get RequestMapping value string, here get "/douglas/**"
    String matchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    // Cut off prefix "/douglas/**"
    String finalPath = apm.extractPathWithinPattern(matchPattern, servletPath);
    return sessionService.redirectTo(request, finalPath);
  }
  
  /**
   * Using annotation to set redirect status code 301.
   * 
   * Since Spring 4.0 ?
   * 
   * http://localhost:8080/SessionManager/douglas5/hello
   * 
   */
  @RequestMapping(value = "/douglas5/**", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
  public String redirectRequest5(HttpServletRequest request) {
    String servletPath = request.getServletPath();
    AntPathMatcher apm = new AntPathMatcher();
    // Get RequestMapping value string, here get "/douglas/**"
    String matchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    // Cut off prefix "/douglas/**"
    String finalPath = apm.extractPathWithinPattern(matchPattern, servletPath);
    return "redirect:/" + finalPath;
  }
  

}
