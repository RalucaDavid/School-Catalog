package org.school.server.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JWTGenerator {

    private static Key SECRET_KEY;
    private static final long EXPIRATION_TIME_MILLISECONDS = 86400000; // 24 hours

    public static void setSecret(String key)
    {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SECRET_KEY = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    public static String generate(String userId)
    {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME_MILLISECONDS);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String parse(String token)
    {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }
}
