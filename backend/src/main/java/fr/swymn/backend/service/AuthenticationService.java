package fr.swymn.backend.service;

import fr.swymn.backend.dto.UserCredential;
import fr.swymn.backend.model.Token;

public interface AuthenticationService {
    Token authenticateUser(UserCredential userCredential) throws BadCredential;
}
