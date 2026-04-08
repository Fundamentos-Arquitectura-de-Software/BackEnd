package com.acme.backendfreshsense.accounts.infrastructure.web;

import com.acme.backendfreshsense.accounts.application.dto.LoginRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserRegistrationRequest;
import com.acme.backendfreshsense.accounts.application.dto.UserResponse;
import com.acme.backendfreshsense.accounts.application.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(accountService.login(request));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(accountService.getByEmail(email));
    }
}
