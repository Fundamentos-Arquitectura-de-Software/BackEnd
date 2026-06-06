package com.acme.backendfreshsense.accounts.application.dto;

import com.acme.backendfreshsense.accounts.domain.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos del usuario autenticado")
public class UserResponse {

    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @Schema(description = "Correo electrónico del usuario", example = "maria@freshsense.pe")
    private String email;

    @Schema(description = "Nombre completo del usuario", example = "María López")
    private String fullName;

    @Schema(description = "Rol asignado al usuario", example = "USER_STANDARD", allowableValues = {"USER_STANDARD", "USER_PREMIUM", "ADMIN"})
    private Role role;

    @Schema(description = "JWT de acceso (presente solo en login/register; null en GET /me)", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    private String refreshToken;

    public UserResponse() {}

    public UserResponse(Long id, String email, String fullName, Role role, String token, String refreshToken) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public Long getId()           { return id; }
    public String getEmail()      { return email; }
    public String getFullName()   { return fullName; }
    public Role getRole()         { return role; }
    public String getToken()      { return token; }
    public String getRefreshToken() { return refreshToken; }
}
