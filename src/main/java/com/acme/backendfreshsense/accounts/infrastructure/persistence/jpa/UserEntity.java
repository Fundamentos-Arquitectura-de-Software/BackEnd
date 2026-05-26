package com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa;

import com.acme.backendfreshsense.accounts.domain.model.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER_STANDARD;

    protected UserEntity() {}

    public UserEntity(String email, String password, String fullName, Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }

    public Long getId()            { return id; }
    public String getEmail()       { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getPassword()    { return password; }
    public void setPassword(String p) { this.password = p; }
    public String getFullName()    { return fullName; }
    public void setFullName(String f) { this.fullName = f; }
    public Role getRole()          { return role; }
    public void setRole(Role r)    { this.role = r; }
}
