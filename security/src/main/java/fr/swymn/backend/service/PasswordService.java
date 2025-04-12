package fr.swymn.backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordEncoder encoder;

    public PasswordService() {
        this.encoder = new BCryptPasswordEncoder();
    }

    public String encode(final String password) {
        return encoder.encode(password);
    }

    public boolean matches(final String rawPassword, final String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
