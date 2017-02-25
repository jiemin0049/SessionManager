Quick start unit test:

1. start de.douglas.springboot.Applcation.java in IDE or at project root directory execute mvn spring-boot:run

2. Execute unit test de.douglas.springboot.SessionManagerTest.java


In this project there are following rest services:

1. Create a session, session id will be created at service side and returned back.

  A example for rest client test:

  URL: http://localhost:8080/SessionManager/session/
  Method: POST
  Headers: Content-type  application/json
  body (json):
  {
     "name"  : "JoJo",
     "email" : "jojo@douglas.de"
  }

  After execution a session id is returned in Response body.    


2. Get all sessions which are saved in session manager.

  A example for rest client test:
  
  URL: http://localhost:8080/SessionManager/session/
  Method: GET
  After execution all sessions are shown in Response body.    

3. Get a session with given id.  

  A example for rest client test:
  
  URL: http://localhost:8080/SessionManager/session/14
  Method: GET
  After execution a session is shown in Response body.

4. Delete a session with given id.

   A example for rest client test:
  
  URL: http://localhost:8080/SessionManager/session/1
  Method: DELETE

5. Delete all sessions.  

   A example for rest client test:
  
   URL: http://localhost:8080/SessionManager/session
   Method: DELETE

6. Update attributes of given session.

   A example for rest client test:
  
   URL: http://localhost:8080/SessionManager/session/1
   Method: PUT
   Headers: Content-type  application/json
   body (json):
   {
     "name"  : "JoJo_new",
     "email" : "jojo_new@douglas.de",
     "product" : "new product"
   }

   After execution the session with all attributes is shown in Response body.

7. Set expiration to true for a given session. 

   A example for rest client test:

   URL: http://localhost:8080/SessionManager/session/expiration/1
   Method: PUT

   

