package fr.swymn.backend.controller;

import fr.swymn.backend.dto.UserCredential;
import fr.swymn.backend.model.Token;
import fr.swymn.backend.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(final @Valid @RequestBody UserCredential credential) {
        var token = userService.authenticateUser(credential);
        return ResponseEntity.ok(token);
    }
}
