package com.acme.backendfreshsense.accounts.infrastructure.web;

import com.acme.backendfreshsense.accounts.application.dto.LoginRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserRegistrationRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserResponse;
import com.acme.backendfreshsense.accounts.application.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Registro, inicio de sesión y gestión de sesión de usuarios")
@RestController
@RequestMapping("/api/accounts")
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea una cuenta nueva. Devuelve los datos del usuario y establece cookies HttpOnly `authToken` (15 min) y `refreshToken` (7 días)."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = UserRegistrationRequest.class),
            examples = @ExampleObject(
                name = "Ejemplo de registro",
                value = """
                        {
                          "email": "maria@freshsense.pe",
                          "password": "Secret1234",
                          "fullName": "María López"
                        }"""
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Usuario creado exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 1,
                              "email": "maria@freshsense.pe",
                              "fullName": "María López",
                              "role": "USER_STANDARD",
                              "token": "eyJhbGciOiJIUzI1NiJ9..."
                            }"""
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos (email mal formado, contraseña sin mayúscula/número, campos vacíos)",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value = "{\"password\": \"La contraseña debe contener al menos una mayúscula y un número\", \"email\": \"El email no es válido\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El email ya está registrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"El email ya está en uso\"}"))
        )
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request,
                                                  HttpServletResponse response) {
        UserResponse resp = accountService.register(request);
        setAccessCookie(response, resp.getToken());
        setRefreshCookie(response, resp.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica al usuario con email y contraseña. Devuelve los datos del usuario y establece cookies HttpOnly `authToken` y `refreshToken`."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = LoginRequest.class),
            examples = @ExampleObject(
                name = "Ejemplo de login",
                value = """
                        {
                          "email": "maria@freshsense.pe",
                          "password": "Secret1234"
                        }"""
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sesión iniciada correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 1,
                              "email": "maria@freshsense.pe",
                              "fullName": "María López",
                              "role": "USER_STANDARD",
                              "token": "eyJhbGciOiJIUzI1NiJ9..."
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Campos vacíos o email mal formado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"email\": \"El email no es válido\"}"))),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Credenciales inválidas\"}")))
    })
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletResponse response) {
        UserResponse resp = accountService.login(request);
        setAccessCookie(response, resp.getToken());
        setRefreshCookie(response, resp.getRefreshToken());
        return ResponseEntity.ok(resp);
    }

    @Operation(
        summary = "Renovar access token",
        description = "Genera un nuevo `authToken` usando el `refreshToken` de la cookie. No requiere body."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Token renovado correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserResponse.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "Refresh token ausente, expirado o revocado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"Refresh token inválido\"}")))
    })
    @PostMapping("/refresh")
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

    @Operation(
        summary = "Cerrar sesión",
        description = "Revoca el refresh token y elimina ambas cookies (`authToken` y `refreshToken`). No requiere body."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Sesión cerrada correctamente — sin cuerpo de respuesta")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractCookie(request, "refreshToken");
        if (refreshToken != null) {
            accountService.revokeRefreshToken(refreshToken);
        }
        clearCookie(response, "authToken", "/");
        clearCookie(response, "refreshToken", "/api/accounts/refresh");
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener usuario autenticado",
        description = "Devuelve el perfil del usuario actualmente autenticado. Requiere cookie `authToken` o header `Authorization: Bearer <token>`."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Datos del usuario autenticado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserResponse.class),
                examples = @ExampleObject(
                    value = """
                            {
                              "id": 1,
                              "email": "maria@freshsense.pe",
                              "fullName": "María López",
                              "role": "USER_STANDARD",
                              "token": null
                            }"""
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Token ausente o expirado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = "{\"error\": \"No autenticado\"}")))
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @Parameter(hidden = true) @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(accountService.getByEmail(email));
    }

    // --- helpers ---

    private void setAccessCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    private void setRefreshCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/accounts/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response, String name, String path) {
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        cookie.setPath(path);
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
