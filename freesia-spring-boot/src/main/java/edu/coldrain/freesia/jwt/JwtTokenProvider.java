package edu.coldrain.freesia.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Component
@Getter
@PropertySource("/jwt.properties")
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String createJwtToken(String subject) {
        final Date issuedAt = new Date();
        final Date expiration = new Date(issuedAt.getTime() + Duration.ofHours(2).toMillis());

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject(subject)
                .setIssuer("coldrain-f")
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}