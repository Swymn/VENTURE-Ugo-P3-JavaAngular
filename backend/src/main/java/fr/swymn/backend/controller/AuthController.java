package fr.swymn.backend.controller;

import fr.swymn.backend.dto.UserCredential;
import fr.swymn.backend.model.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody UserCredential credential) {
        return ResponseEntity.ok(new Token("fake-jwt-token"));
    }
}
