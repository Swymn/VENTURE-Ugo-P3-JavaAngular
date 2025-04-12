package fr.swymn.backend.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final Collection<UserDetails> users;

    public UserService(final PasswordService passwordService) {
        users = List.of(
                User.withUsername("user")
                        .password(passwordService.encode("password"))
                        .roles("USER")
                        .build(),
                User.withUsername("admin")
                        .password(passwordService.encode("password"))
                        .roles("ADMIN")
                        .build()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
