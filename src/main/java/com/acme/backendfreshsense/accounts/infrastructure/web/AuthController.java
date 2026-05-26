package com.acme.backendfreshsense.accounts.infrastructure.web;

import com.acme.backendfreshsense.accounts.application.dto.LoginRequest;
import com.acme.backendfreshsense.accounts.application.dto.RefreshTokenRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserRegistrationRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserResponse;
import com.acme.backendfreshsense.accounts.application.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Autenticación y gestión de usuarios")
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request,
                                                  HttpServletResponse response) {
        UserResponse resp = accountService.register(request);
        setAccessCookie(response, resp.getToken());
        setRefreshCookie(response, resp.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletResponse response) {
        UserResponse resp = accountService.login(request);
        setAccessCookie(response, resp.getToken());
        setRefreshCookie(response, resp.getRefreshToken());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar access token con refresh token")
    public ResponseEntity<UserResponse> refresh(HttpServletRequest request,
                                                 HttpServletResponse response) {
        String refreshToken = extractCookie(request, "refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserResponse resp = accountService.refresh(refreshToken);
        setAccessCookie(response, resp.getToken());
        setRefreshCookie(response, resp.getRefreshToken());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión y revocar tokens")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractCookie(request, "refreshToken");
        if (refreshToken != null) {
            accountService.revokeRefreshToken(refreshToken);
        }
        clearCookie(response, "authToken");
        clearCookie(response, "refreshToken");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener usuario autenticado")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(accountService.getByEmail(email));
    }

    // --- helpers ---

    private void setAccessCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60); // 15 minutos
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    private void setRefreshCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/accounts/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 días
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String extractCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) return null;
        for (Cookie c : request.getCookies()) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }
}
