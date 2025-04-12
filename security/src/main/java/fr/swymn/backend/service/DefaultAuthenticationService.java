package fr.swymn.backend.service;

import fr.swymn.backend.dto.UserCredential;
import fr.swymn.backend.model.Token;
import fr.swymn.backend.security.TokenGenerator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final TokenGenerator tokenGenerator;
    private final PasswordService passwordService;

    public DefaultAuthenticationService(
            final UserDetailsService userDetailsService,
            final TokenGenerator tokenGenerator,
            final PasswordService passwordService
    ) {
        this.userDetailsService = userDetailsService;
        this.tokenGenerator = tokenGenerator;
        this.passwordService = passwordService;
    }

    @Override
    public Token authenticateUser(final UserCredential userCredential) throws BadCredential {
        try {
            var userDetails = userDetailsService.loadUserByUsername(userCredential.getLogin());
            if (passwordService.matches(userCredential.getPassword(), userDetails.getPassword())) {
                return tokenGenerator.generateToken(userDetails);
            } else {
                throw new BadCredential();
            }
        } catch (final UsernameNotFoundException e) {
            throw new BadCredential();
        }
    }
}
