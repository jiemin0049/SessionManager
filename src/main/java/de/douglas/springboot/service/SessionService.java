package de.douglas.springboot.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import de.douglas.springboot.model.DouglasSession;

public interface SessionService {

  DouglasSession findById(String id);

  String createSession(Map<String, String> attributeMap);

  DouglasSession updateSession(String id, Map<String, String> attributeMap);

  void deleteSession(String id);

  void clearSessions();

  List<DouglasSession> getAllSessions();

  void expireSession(String id);

  void expireAllSessions();

  String redirectTo(HttpServletRequest request, String finalPath);

}
