package com.acme.backendfreshsense.accounts.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear una nueva cuenta de usuario")
public class UserRegistrationRequest {

    @Schema(description = "Correo electrónico único del usuario", example = "maria@freshsense.pe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @Schema(description = "Contraseña: mínimo 8 caracteres, al menos 1 mayúscula y 1 número", example = "Secret1234", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
             message = "La contraseña debe contener al menos una mayúscula y un número")
    private String password;

    @Schema(description = "Nombre completo del usuario", example = "María López", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ']+$",
            message = "El nombre completo no puede contener números, ni caracteres"
    )
    private String fullName;

    public UserRegistrationRequest() {}

    public UserRegistrationRequest(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
