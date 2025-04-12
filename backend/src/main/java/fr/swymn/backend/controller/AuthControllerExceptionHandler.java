package fr.swymn.backend.controller;

import java.util.HashMap;
import java.util.Map;

import fr.swymn.backend.service.BadCredential;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class AuthControllerExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        log.debug("Handling validation exception: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("username", ex.getMessage());
        log.debug("Handling username not found exception: {}", error);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(BadCredential.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialException(BadCredential ex) {
        Map<String, String> error = new HashMap<>();
        error.put("username", ex.getMessage());
        log.debug("Handling bad credential exception: {}", error);
        return ResponseEntity.badRequest().body(error);
    }
}
