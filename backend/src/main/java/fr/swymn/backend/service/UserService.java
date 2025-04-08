package fr.swymn.backend.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.swymn.backend.dto.UserCredential;
import fr.swymn.backend.model.Token;

public interface UserService {
    Token authenticateUser(UserCredential userCredential) throws UsernameNotFoundException;
}
