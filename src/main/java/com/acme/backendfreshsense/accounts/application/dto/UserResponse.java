package com.acme.backendfreshsense.accounts.application.dto;

import com.acme.backendfreshsense.accounts.domain.model.Role;

public class UserResponse {

    private Long id;
    private String email;
    private String fullName;
    private Role role;
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
