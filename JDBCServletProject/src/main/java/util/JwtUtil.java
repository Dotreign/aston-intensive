package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.security.Key;

public class JwtUtil {

    private static JwtUtil instance;

    private JwtUtil() {

    }

    public static JwtUtil getInstance() {
        if (instance == null) {
            instance = new JwtUtil();
        }
        return instance;
    }

    private final String secret = "OdX2zrnTxnQLT3mXP1LpcsTJR0HFXywd";

    public String generateToken(String username) {
        Key key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes());
        Claims claims = Jwts.claims().setSubject(username);
        String jwt =Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
        return jwt;
    }

    public String extractUsername(String token) {
        Key key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
