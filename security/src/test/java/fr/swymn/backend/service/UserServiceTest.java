package fr.swymn.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserServiceTest {

    private UserDetailsService userDetailsService;

    @BeforeEach
    void setup() {
        userDetailsService = new UserService(new PasswordService());
    }

    @Test
    void loadUserByUsername_returnUser_existingUser() throws UsernameNotFoundException {
        // GIVEN a username
        var username = "user";

        // WHEN the user tries to load the user by username
        var userDetails = userDetailsService.loadUserByUsername(username);

        // THEN the user details are returned
        Assertions.assertNotNull(userDetails);
    }

    @Test
    void loadUserByUsername_throwException_nonExistingUser() {
        // GIVEN a non-existing username
        var username = "nonExistingUser";

        // WHEN the user tries to load the user by username
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }
}
