package fr.swymn.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
class TokenGeneratorTest {

    private TokenGenerator tokenGenerator;
    private String privateKey;

    @BeforeEach
    void setUp() {
        privateKey = "testPrivateKeytestPrivateKeytestPrivateKeytestPrivateKeytestPrivateKeytestPrivateKeytestPrivateKey";
        tokenGenerator = new JwtTokenGenerator(privateKey, 3600);
    }

    @Test
    void generateToken_returnValidToken_validInputs() {
        // GIVEN a user
        var user = new User("username", "password", Collections.emptyList());

        // WHEN we generate a token
        var token = tokenGenerator.generateToken(user);

        // THEN the token is not null
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.getAccessToken().startsWith("ey"));
    }

    @Test
    void generateToken_checkTokenContent_validInputs() {
        // GIVEN a user
        var user = new User("username", "password", Collections.emptyList());
        // AND an encoded private key
        var keyBytes = Decoders.BASE64.decode(privateKey);
        var encodedPrivateKey = Keys.hmacShaKeyFor(keyBytes);

        // WHEN we generate a token
        var token = tokenGenerator.generateToken(user);

        // Decode the token
        var claims = Jwts.parser()
                .setSigningKey(encodedPrivateKey)
                .build()
                .parseClaimsJws(token.getAccessToken())
                .getBody();

        // THEN verify the token content
        Assertions.assertEquals("username", claims.getSubject());
        Assertions.assertTrue(claims.getExpiration().after(new Date()));
        Assertions.assertNotNull(claims.get("roles"));
    }
}
