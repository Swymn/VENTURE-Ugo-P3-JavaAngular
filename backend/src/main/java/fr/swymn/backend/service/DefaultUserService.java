package fr.swymn.backend.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.swymn.backend.dto.UserCredential;
import fr.swymn.backend.model.Token;

@Service
public class DefaultUserService implements UserService {

    @Override
    public Token authenticateUser(UserCredential userCredential) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
