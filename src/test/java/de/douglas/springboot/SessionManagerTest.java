package de.douglas.springboot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.douglas.springboot.model.DouglasSession;

public class SessionManagerTest {

  private static final String REST_SERVICE_URI = "http://localhost:8080/SessionManager";

  private String beforeClassSessionId;

  @Before
  public void beforeClass() {
    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> map = new HashMap<>();
    map.put("name", "JoJo");
    map.put("email", "jojo@douglas.de");
    // restTemplate.postForLocation(REST_SERVICE_URI + "/session/", dglsSession, DouglasSession.class);
    HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map);

    ResponseEntity<String> response = restTemplate.postForEntity(REST_SERVICE_URI + "/session/", request, String.class);
    beforeClassSessionId = response.getBody();
  }

  @After
  public void afterClass() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.delete(REST_SERVICE_URI + "/session/");
  }

  @Test
  public void getAllSessions() {
    RestTemplate restTemplate = new RestTemplate();
    List<DouglasSession> sessionList = restTemplate.getForObject(REST_SERVICE_URI + "/session/", List.class);
    assertEquals(1, sessionList.size());
  }

  /**
   * Get a session with given id.
   */
  @Test
  public void getSession() {
    RestTemplate restTemplate = new RestTemplate();
    DouglasSession session = restTemplate.getForObject(REST_SERVICE_URI + "/session/" + beforeClassSessionId, DouglasSession.class);
    assertEquals(beforeClassSessionId, session.getSessionId());
    Map<String, String> map = session.getAttributes();
    assertEquals("JoJo", map.get("name"));
    assertEquals("jojo@douglas.de", map.get("email"));
  }

  /**
   * Test creating a session. (POST)
   */
  @Test
  public void createSession() {
    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> map = new HashMap<>();
    map.put("name", "HuHu");
    map.put("email", "HuHu@douglas.de");
    HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(map);

    ResponseEntity<String> response = restTemplate.postForEntity(REST_SERVICE_URI + "/session/", request, String.class);
    String sessionId = response.getBody();

    // 1. add a session, check session size.
    List<DouglasSession> sessionList = restTemplate.getForObject(REST_SERVICE_URI + "/session/", List.class);
    assertEquals(2, sessionList.size());

    // 2. check the added session attribute.
    DouglasSession dglsSession = restTemplate.getForObject(REST_SERVICE_URI + "/session/" + sessionId, DouglasSession.class);
    map = dglsSession.getAttributes();
    assertEquals("HuHu", map.get("name"));
    assertEquals("HuHu@douglas.de", map.get("email"));
  }

  /**
   * Delete a session.
   */
  @Test
  public void deleteSession() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.delete(REST_SERVICE_URI + "/session/" + beforeClassSessionId);

    List<DouglasSession> sessionList = restTemplate.getForObject(REST_SERVICE_URI + "/session/", List.class);
    assertNull(sessionList);
  }

  /**
   * Update a session. (PUT)
   */
  @Test
  public void updateSession() {
    RestTemplate restTemplate = new RestTemplate();

    // 1. Update session attribute.
    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put("name", "JoJo_new");
    attributes.put("email", "jojo@douglas.de");
    attributes.put("DIOR", "60");
    restTemplate.put(REST_SERVICE_URI + "/session/" + beforeClassSessionId, attributes);

    // 2. check the added session attribute.
    DouglasSession dglsSession = restTemplate.getForObject(REST_SERVICE_URI + "/session/" + beforeClassSessionId, DouglasSession.class);
    Map<String, String> map = dglsSession.getAttributes();
    assertEquals("JoJo_new", map.get("name"));
    assertEquals("jojo@douglas.de", map.get("email"));
    assertEquals("60", map.get("DIOR"));
  }

  /**
   * Expire a session. (PUT)
   */
  @Test
  public void expireSession() {
    RestTemplate restTemplate = new RestTemplate();

    // 1. expire a session.
    restTemplate.put(REST_SERVICE_URI + "/session/expiration/" + beforeClassSessionId, null);

    // 2. check the session status.
    DouglasSession dglsSession = restTemplate.getForObject(REST_SERVICE_URI + "/session/" + beforeClassSessionId, DouglasSession.class);
    assertEquals(true, dglsSession.isExpiration());
  }

}
