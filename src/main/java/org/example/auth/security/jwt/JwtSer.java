package org.example.auth.security.jwt;
import io.jsonwebtoken.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.auth.Dto.JwtAutenticationDto;
import org.springframework.beans.factory.annotation.Value;
import  org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtSer {
    @Value("${app.jwt.secret:fIwXMy5LxcvgAt5NPDYMXNXxr9ygARb4jjr9yskAonK}") 
    private String jwtSecret;
    private static final Logger LOGGER = LogManager.getLogger(JwtSer.class);

    public JwtAutenticationDto generateAuthToken(String email){
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        JwtAutenticationDto jwtDto = new JwtAutenticationDto();
        try {
            jwtDto.setToken(generateJwtToken(email));
            jwtDto.setRefreshToken(generateRefreshToken(email));
        } catch (Exception e) {
            LOGGER.error("Error generating tokens for user: " + email, e);
            throw new RuntimeException("Failed to generate JWT", e);
        }
        return jwtDto;
    }

    public JwtAutenticationDto refreshBaseToken(String email, String refreshToken){
        JwtAutenticationDto jwtDto = new JwtAutenticationDto();
        jwtDto.setToken(generateJwtToken(email));
        jwtDto.setRefreshToken(generateRefreshToken(email));
        return  jwtDto;
    }

    public  String getEmailFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (ExpiredJwtException expEx){
            LOGGER.error("Expired JwtException", expEx);
        } catch (UnsupportedJwtException expEx){
            LOGGER.error("Unsupported JwtException", expEx);
        } catch (MalformedJwtException expEx){
            LOGGER.error("Malformed JwtException", expEx);
        } catch (SecurityException expEx){
            LOGGER.error("Security Exception", expEx);
        } catch (Exception expEx){
            LOGGER.error("Invalid", expEx);
        }

        return  false;
    }

    private String generateJwtToken(String email){
        Date date = Date.from(LocalDateTime.now().plusMinutes(15).atZone(ZoneId.systemDefault()).toInstant());
        return  Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }

    private String generateRefreshToken(String email){
        Date date = Date.from(LocalDateTime.now().plusDays(14).atZone(ZoneId.systemDefault()).toInstant());
        return  Jwts.builder()
                .subject(email)
                .expiration(date)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey(){
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid JWT Secret Key format", e);
            throw new RuntimeException("Invalid JWT configuration", e);
        }
    }
}
