package de.douglas.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.douglas.springboot.service.SessionService;

@RestController
public class RedirectToController {

  @Autowired
  SessionService sessionService;

  /**
   * Get all sessions.
   * 
   * @return
   */
  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String hello() {
    return "hello_0";
  }

}