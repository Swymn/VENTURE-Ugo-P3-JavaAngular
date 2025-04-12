package fr.swymn.backend.security;

import fr.swymn.backend.model.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenGenerator implements TokenGenerator {

    private final String privateKey;
    private final long expirationTime;

    public JwtTokenGenerator(
            @Value("${jwt.privateKey}") String privateKey,
            @Value("${jwt.expirationTime}") long expirationTime
    ) {
        this.privateKey = privateKey;
        this.expirationTime = expirationTime;
    }

    @Override
    public Token generateToken(final UserDetails user) {
        var now = new Date();
        var expirationDate = new Date(now.getTime() + expirationTime);

        var token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", extractRolesFromAuthorities(user.getAuthorities()))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, encodePrivateKey(privateKey))
                .compact();

        return new Token(token);
    }

    private Key encodePrivateKey(final String privateKey) {
        var keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private List<SimpleGrantedAuthority> extractRolesFromAuthorities(final Collection<? extends GrantedAuthority> authoritiesCollection) {
        return authoritiesCollection.stream()
                .map(GrantedAuthority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
