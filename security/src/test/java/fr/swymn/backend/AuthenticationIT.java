package fr.swymn.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationIT {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    @Test
    void authenticateUser_shouldAuthenticated_existingUser() throws IOException, InterruptedException {
        // GIVEN a user with username and password
        var username = "user";
        var password = "password";

        // WHEN the user tries to authenticate
        var response = httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/api/auth/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"login\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        // THEN the response should be 200 OK
        // AND a token should be returned
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.body().contains("accessToken"));
    }

    @Test
    void authenticateUser_shouldReturnBadRequest_wrongPassword() throws IOException, InterruptedException {
        // GIVEN a user with username and password
        var username = "user";
        var password = "wrong-password";

        // WHEN the user tries to authenticate
        var response = httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:" + port + "/api/auth/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"login\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        // THEN the response should be 200 OK
        // AND a token should be returned
        Assertions.assertEquals(400, response.statusCode());
        var errorResponse = MAPPER.readTree(response.body());
        Assertions.assertEquals("Username or password is incorrect", errorResponse.get("username").asText());
    }
}
