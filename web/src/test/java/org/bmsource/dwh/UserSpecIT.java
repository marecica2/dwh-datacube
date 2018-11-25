package org.bmsource.dwh;

import org.bmsource.dwh.users.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class UserSpecIT extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void getUser() {
        String url = createURLWithPort("/users");
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(response);
    }

    @Test
    public void postUser() {
        User user = new User("Marek", "Balla");
        HttpEntity<User> entity = new HttpEntity<User>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users"),
                HttpMethod.POST, entity, String.class);

        String location = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        System.out.println(location);

        String url = createURLWithPort("/users/1");
        HttpEntity<String> e = new HttpEntity<String>(null, headers);
        response = restTemplate.exchange(url, HttpMethod.GET, e, String.class);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}