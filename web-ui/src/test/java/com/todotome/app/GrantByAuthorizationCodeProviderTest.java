package com.todotome.app;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class GrantByAuthorizationCodeProviderTest extends OAuth2Test {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void getJwtTokenByAuthorizationCode() throws JsonParseException, JsonMappingException, IOException, URISyntaxException, InvalidJwtException {
        String userName = "app_client";
        String password = "nopass";

        String redirectUrl = "http://localhost:" + port + "/resources/user";
        ResponseEntity<String> response = new TestRestTemplate(userName, password).postForEntity("http://localhost:" + port + "/oauth/authorize?response_type=code&client_id=normal-app&redirect_uri={redirectUrl}", null, String.class, redirectUrl);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<String> setCookie = response.getHeaders().get("Set-Cookie");
        String jSessionIdCookie = setCookie.get(0);
        String cookieValue = jSessionIdCookie.split(";")[0];

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookieValue);
        response = new TestRestTemplate(userName, password).postForEntity("http://localhost:" + port
                + "oauth/authorize?response_type=code&client_id=normal-app&redirect_uri={redirectUrl}&user_oauth_approval=true&authorize=Authorize", new HttpEntity<>(headers), String.class, redirectUrl);
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNull(response.getBody());
        String location = response.getHeaders().get("Location").get(0);
        URI locationURI = new URI(location);
        String query = locationURI.getQuery();

        location = "http://localhost:" + port + "/oauth/token?" + query + "&grant_type=authorization_code&client_id=normal-app&redirect_uri={redirectUrl}";

        response = new TestRestTemplate("normal-app", "").postForEntity(location, new HttpEntity<>(new HttpHeaders()), String.class, redirectUrl);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        HashMap jwtMap = new ObjectMapper().readValue(response.getBody(), HashMap.class);
        String accessToken = (String) jwtMap.get("access_token");

        JwtContext jwtContext = jwtConsumer.process(accessToken);

        logJWTClaims(jwtContext);

        assertEquals(userName, jwtContext.getJwtClaims().getClaimValue("user_name"));

        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        response = new TestRestTemplate().exchange("http://localhost:" + port + "/resources/client", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = new TestRestTemplate().exchange("http://localhost:" + port + "/resources/user", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = new TestRestTemplate().exchange("http://localhost:" + port + "/resources/principal", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
        assertEquals(userName, response.getBody());

        response = new TestRestTemplate().exchange("http://localhost:" + port + "/resources/roles", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
        assertEquals("[{\"authority\":\"ROLE_USER\"}]", response.getBody());
    }

}