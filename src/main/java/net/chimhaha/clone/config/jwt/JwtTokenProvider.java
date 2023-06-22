package net.chimhaha.clone.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.chimhaha.clone.config.auth.CustomOAuth2User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final int expirationTime;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expired_date}") int expirationTime) {
        this.secret = secret;
        this.expirationTime = expirationTime;
    }

    public String create(Authentication authentication) {
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expirationTime);
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        String jwt = Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    }
}
