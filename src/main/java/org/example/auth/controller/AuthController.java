package org.example.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.auth.Dto.JwtAutenticationDto;
import org.example.auth.Dto.UserCredentialsDto;
import org.example.auth.Dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;
import org.example.auth.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private ResponseCookie generateJwtCookie(String tokenName, String tokenValue, long expirationMs) {
        return ResponseCookie.from(tokenName, tokenValue)
                .httpOnly(true)                    // Защита от XSS
                .secure(true)                     // true только для HTTPS
                .path("/")                         // Доступно для всего сайта
                .maxAge(Duration.ofMillis(expirationMs))
                .sameSite("Strict")               // Защита от CSRF
                .build();
    }

    private ResponseCookie getCleanCookie(String tokenName) {
        return ResponseCookie.from(tokenName, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)                        // Удалить cookie
                .sameSite("Strict")
                .build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserCredentialsDto userCredentialsDto){
        try{
            JwtAutenticationDto jwtAutenticationDto = userService.signIn(userCredentialsDto);

            // Создаём cookie с токенами
            ResponseCookie accessTokenCookie = generateJwtCookie("accessToken",
                    jwtAutenticationDto.getToken(), 900000);

            ResponseCookie refreshTokenCookie = generateJwtCookie("refreshToken",
                    jwtAutenticationDto.getRefreshToken(),  604800000);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(Map.of(
                            "message", "Login successful"
                    ));

        } catch (AuthenticationException e){
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e){  
            e.printStackTrace(); 
            return ResponseEntity.status(500).body(Map.of("error", "Server error: " + e.getMessage()));
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No refresh token"));
        }

        try {
            JwtAutenticationDto newTokens = userService.refreshToken(refreshToken);

            ResponseCookie newAccessTokenCookie = generateJwtCookie("accessToken",
                    newTokens.getToken(), 900000);

            ResponseCookie newRefreshTokenCookie = generateJwtCookie("refreshToken",
                    newTokens.getRefreshToken(), 604800000);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
                    .body(Map.of("message", "Token refreshed successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .header(HttpHeaders.SET_COOKIE, getCleanCookie("accessToken").toString())
                    .header(HttpHeaders.SET_COOKIE, getCleanCookie("refreshToken").toString())
                    .body(Map.of("error", "Token refresh failed"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Удаляем cookie
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, getCleanCookie("accessToken").toString())
                .header(HttpHeaders.SET_COOKIE, getCleanCookie("refreshToken").toString())
                .body(Map.of("message", "Logout successful"));
    }
}
