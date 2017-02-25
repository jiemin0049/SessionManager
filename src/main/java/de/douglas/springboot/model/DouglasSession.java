package de.douglas.springboot.model;

import java.util.HashMap;
import java.util.Map;

public class DouglasSession {
  private String sessionId;
  private boolean expiration;
  private Map<String, String> attributes = new HashMap<>();

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public boolean isExpiration() {
    return expiration;
  }

  public void setExpiration(boolean expiration) {
    this.expiration = expiration;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

}
