package de.douglas.springboot;

import javax.servlet.DispatcherType;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.douglas.springboot.filter.DouglasRewriteFilter;
import de.douglas.springboot.filter.PageNotFoundFilter;

@Configuration
public class WebConfig {

  @Bean
  public FilterRegistrationBean greetingFilterRegistrationBean() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setName("PageNotFound");
    PageNotFoundFilter notFoundFilter = new PageNotFoundFilter();
    registrationBean.setFilter(notFoundFilter);
    registrationBean.setOrder(1);
    //registrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR);
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean helloFilterRegistrationBean() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setName("RewriteFilter");
    DouglasRewriteFilter rewriteFilter = new DouglasRewriteFilter();
    registrationBean.setFilter(rewriteFilter);
    registrationBean.setOrder(2);
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }

}
