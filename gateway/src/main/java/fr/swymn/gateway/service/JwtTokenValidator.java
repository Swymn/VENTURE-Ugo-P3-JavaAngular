package fr.swymn.gateway.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenValidator implements TokenValidator {

    private final String privateKey;

    public JwtTokenValidator(final @Value("${jwt.privateKey}") String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public boolean isValid(final String token) {
        try {
            var claims = Jwts.parser()
                    .setSigningKey(encodePrivateKey(privateKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            var expirationDate = claims.getExpiration();
            return !expirationDate.before(new Date());
        } catch (SignatureException ex) {
            log.error(ex.getMessage());
            return false;
        } catch (Exception ex) {
            log.error("Token validation failed: {}", ex.getMessage());
            return false;
        }
    }

    private Key encodePrivateKey(final String privateKey) {
        var keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
