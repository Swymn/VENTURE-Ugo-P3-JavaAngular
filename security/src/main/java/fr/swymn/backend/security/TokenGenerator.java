package fr.swymn.backend.security;

import fr.swymn.backend.model.Token;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenGenerator {
    Token generateToken(UserDetails userDetails);
}
