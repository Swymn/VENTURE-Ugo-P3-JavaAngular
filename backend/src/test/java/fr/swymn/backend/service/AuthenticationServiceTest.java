package fr.swymn.backend.service;

import fr.swymn.backend.dto.UserCredential;
import fr.swymn.backend.model.Token;
import fr.swymn.backend.security.TokenGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserDetailsService mockUserDetailsService;
    @Mock
    private TokenGenerator mockTokenGenerator;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new DefaultAuthenticationService(mockUserDetailsService, mockTokenGenerator, new PasswordService());
    }

    private void mockToken() {
        Mockito
                .when(mockTokenGenerator.generateToken(Mockito.any()))
                .thenReturn(new Token("access-token"));
    }

    @Test
    void authenticateUser_returnToken_existingUser() throws BadCredential {
        // MOCK
        mockToken();
        Mockito
                .when(mockUserDetailsService.loadUserByUsername(Mockito.anyString()))
                .thenReturn(new User("user", "password", Collections.emptyList()));

        // GIVEN a user with valid credentials
        var user = new UserCredential("username", "password");

        // WHEN the user tries to authenticate
        var token = authenticationService.authenticateUser(user);

        // THEN a token is returned
        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.getAccessToken().isEmpty());
    }

    @Test
    void authenticateUser_throwException_nonExistingUser() {
        Mockito
                .when(mockUserDetailsService.loadUserByUsername(Mockito.anyString()))
                .thenThrow(new BadCredential());

        // GIVEN a user with invalid credentials
        var user = new UserCredential("username", "wrong-password");

        // WHEN the user tries to authenticate
        // THEN an exception is thrown
        Assertions.assertThrows(BadCredential.class, () -> {
            authenticationService.authenticateUser(user);
        });
    }
}
