package de.douglas.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.douglas.springboot.model.DouglasSession;
import de.douglas.springboot.service.SessionService;

@RestController
@RequestMapping("/session")
public class SessionManagerController {

  @Autowired
  SessionService sessionService;

  /**
   * Get all sessions.
   * 
   * @return
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<DouglasSession>> getAllSessions() {
    List<DouglasSession> sessions = sessionService.getAllSessions();
    if (sessions.isEmpty()) {
      return new ResponseEntity<List<DouglasSession>>(new ArrayList<DouglasSession>(), HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<DouglasSession>>(sessions, HttpStatus.OK);
  }

  /**
   * Get a session with given session id.
   * 
   * @param id
   *          session id.
   * @return
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<DouglasSession> getSession(@PathVariable("id") String id) {
    DouglasSession session = sessionService.findById(id);
    if (session == null) {
      return new ResponseEntity<DouglasSession>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<DouglasSession>(session, HttpStatus.OK);
  }

  /**
   * Create a session, the created session id is shown response body.
   * 
   * @param session
   *          new session.
   * @return
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<String> createSession(@RequestBody Map<String, String> attributeMap) {
    String id = sessionService.createSession(attributeMap);
    return new ResponseEntity<String>(id, HttpStatus.CREATED);
  }

  /**
   * Delete a session.
   * 
   * @param id
   * @return
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void deleteSession(@PathVariable("id") String id) {
    sessionService.deleteSession(id);
  }

  /**
   * Delete all sessions.
   */
  @RequestMapping(method = RequestMethod.DELETE)
  public void clearSession() {
    sessionService.clearSessions();
  }

  /**
   * Update attributes of a given session.
   * 
   * @param id
   *          session id
   * @param session
   * @return
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<DouglasSession> updateSession(@PathVariable("id") String id, @RequestBody Map<String, String> attributeMap) {

    DouglasSession dglsSession = sessionService.findById(id);

    if (dglsSession == null) {
      return new ResponseEntity<DouglasSession>(HttpStatus.NOT_FOUND);
    }

    dglsSession = sessionService.updateSession(id, attributeMap);
    return new ResponseEntity<DouglasSession>(dglsSession, HttpStatus.OK);
  }

  /**
   * Make a seesion expiration.
   * 
   * @param id
   *          session id.
   */
  @RequestMapping(value = "/expiration/{id}", method = RequestMethod.PUT)
  public void expireSession(@PathVariable("id") String id) {
    sessionService.expireSession(id);
  }

  /**
   * Expire all sessions.
   * 
   */
  @RequestMapping(value = "/expiration", method = RequestMethod.PUT)
  public void expireAllSession() {
    sessionService.expireAllSessions();
  }

}