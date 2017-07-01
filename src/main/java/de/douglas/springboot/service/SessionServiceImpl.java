package de.douglas.springboot.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import de.douglas.springboot.model.DouglasSession;

@Service("sessionService")
public class SessionServiceImpl implements SessionService {

  /** Saves all session in map. */
  private static final Map<String, DouglasSession> sessionMap = new ConcurrentHashMap<>();

  /** counter creates session id. */
  private static final AtomicLong counter = new AtomicLong();

  @Override
  public DouglasSession findById(String id) {
    return sessionMap.get(id);
  }

  @Override
  public String createSession(Map<String, String> attributeMap) {
    String sid = counter.incrementAndGet() + "";
    DouglasSession session = new DouglasSession();
    session.setSessionId(sid);
    session.getAttributes().putAll(attributeMap);
    sessionMap.put(sid, session);
    return sid;
  }

  @Override
  public DouglasSession updateSession(String id, Map<String, String> attributeMap) {
    DouglasSession session = findById(id);
    if (session != null) {
      session.getAttributes().putAll(attributeMap);
    }
    return session;
  }

  @Override
  public void deleteSession(String id) {
    sessionMap.remove(id);
  }

  @Override
  public List<DouglasSession> getAllSessions() {
    return new ArrayList<DouglasSession>(sessionMap.values());
  }

  @Override
  public void clearSessions() {
    sessionMap.clear();
  }

  @Override
  public void expireSession(String id) {
    DouglasSession session = findById(id);
    if (session != null) {
      session.setExpiration(true);
    }
  }

  @Override
  public void expireAllSessions() {
    Collection<DouglasSession> sessions = sessionMap.values();
    for (DouglasSession s : sessions) {
      s.setExpiration(true);
    }
  }

  @Override
  public String redirectTo(HttpServletRequest request, String path) {
    String rd = addPath(request, path);
    return rd;
  }

  private String addPath(HttpServletRequest request, String path) {
    request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY);
    return "redirect:/" + path;
  }

}
