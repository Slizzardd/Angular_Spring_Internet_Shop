package ua.com.alevel.util;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * Utility class for working with JWT (JSON Web Token).
 */
public final class JwtUtil {
    public static final String SECRET_KEY = "j123jkgk234k2o345p35ktY45kvyp3v45mnmk324vok345pSFm34ok6vk34sLpqlm52kl3j46kv24o5kn6mv23m6vkn23m6vl2m456klk2m3m6vm23lk56vm";
    public static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    private JwtUtil() {
        throw new IllegalStateException("This is a utility class");
    }

    public static String extractUsername(String token) {
        if (token.equals("null")){
            throw new JwtException("Failed to parse JWT token");
        }
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            // Handle JWT parsing exceptions
            throw new JwtException("Failed to parse JWT token", e);
        }
    }

    public static String generateJwtToken(String username) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();
        } catch (Exception e) {
            // Handle JWT generation exceptions
            throw new JwtException("Failed to generate JWT token", e);
        }
    }
}