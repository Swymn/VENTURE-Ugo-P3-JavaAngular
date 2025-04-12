package fr.swymn.backend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import fr.swymn.backend.model.Token;
import fr.swymn.backend.service.AuthenticationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService userService;

    @Test
    void loginUser_shouldLoginUser_existingUser() throws Exception {
        // MOCK
        Mockito.when(userService.authenticateUser(Mockito.any()))
                .thenReturn(new Token("accessToken"));

        // GIVEN an auth controller, with a valid user credentials.
        var username = "testuser";
        var password = "testpassword";
        var loginRequest = "{\"login\": \"" + username + "\", \"password\": \"" + password + "\"}";

        // WHEN I call the login endpoint with the valid credentials.
        // THEN I should receive a 200 OK response.
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "{\"login\": \"\", \"password\": \"\"}",
        "{}",
    })
    void loginUser_shouldNotLoginUser_missingUser(String loginRequest) throws Exception {
        // GIVEN an auth controller, with an error payload.
        // WHEN I call the login endpoint with the missing credentials.
        // THEN I should receive a 400 BAD REQUEST response.
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.login").value("Login is required."));
    }

    @Test
    void loginUser_shouldNotLoginUser_passwordTooShort() throws Exception {
        // GIVEN an auth controller, with a missing user credentials.
        var username = "testuser";
        var password = "1234";
        var loginRequest = "{\"login\": \"" + username + "\", \"password\": \"" + password + "\"}";

        // WHEN I call the login endpoint with the missing credentials.
        // THEN I should receive a 400 BAD REQUEST response.
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password must be at least 8 characters long."));
    }

    @Test
    void loginUser_shouldNotLoginuser_wrongPassword() throws Exception {
        // MOCK
        Mockito.when(userService.authenticateUser(Mockito.any()))
                .thenThrow(new UsernameNotFoundException("Invalid username or password."));

        // GIVEN an auth controller, with a valid user credentials.
        var username = "testuser";
        var password = "wrongpassword";
        var loginRequest = "{\"login\": \"" + username + "\", \"password\": \"" + password + "\"}";

        // WHEN I call the login endpoint with the valid credentials.
        // THEN I should receive a 400 BAD REQUEST response.
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("Invalid username or password."));
    }
}
