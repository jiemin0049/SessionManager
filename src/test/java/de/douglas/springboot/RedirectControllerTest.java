package de.douglas.springboot;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import de.douglas.springboot.controller.RedirectController;

public class RedirectControllerTest {

  private static final String DOUGLAS = "/douglas";

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Before
  public void setUp() {
    request = Mockito.mock(HttpServletRequest.class);
    response = Mockito.mock(HttpServletResponse.class);
    when(request.getContextPath()).thenReturn("");
    when(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)).thenReturn(DOUGLAS + "/**");
  }

  /**
   * Verify whether the cutting url is correct. 
   * How to know and do: 
   * 1. Run test method. 
   * 2. At end of test method, method "response.sendRedirect(String)" will be invoked, 
   *    now check the String parameter, whether it is expected. 
   *
   * @param url
   * @throws Exception
   */
  @Test
  public void testRequestRedirect0() throws Exception {
    String url = "/id/1/name/rocket";
    when(request.getServletPath()).thenReturn(DOUGLAS + url);
    RedirectController controller = new RedirectController();
    controller.redirectRequest0(request, response);
    ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
    verify(response).sendRedirect(argument.capture());
    assertEquals(url, argument.getValue());
  }

  @Test
  public void testRequestRedirect2() {
    String redirectUrl = "/Pflege/Gesicht/Tagespflege/index_020102.html";
    String requestUrl = DOUGLAS + redirectUrl;
    when(request.getServletPath()).thenReturn(requestUrl);

    Map<String, String[]> requestParameterMap = new LinkedHashMap<>();
    requestParameterMap.put("id", new String[] { "1" });
    requestParameterMap.put("name", new String[] { "Lancôme Trésor" });
    when(request.getParameterMap()).thenReturn(requestParameterMap);

    RedirectController controller = new RedirectController();
    ModelAndView mav = controller.redirectRequest2(request);
    RedirectView redirectView = (RedirectView) mav.getView();

    assertEquals(redirectUrl, redirectView.getUrl());
    Assert.assertArrayEquals((String[]) redirectView.getAttributesMap().get("id"), new String[] { "1" });
    Assert.assertArrayEquals((String[]) redirectView.getAttributesMap().get("name"), new String[] { "Lancôme Trésor" });
  }
}
