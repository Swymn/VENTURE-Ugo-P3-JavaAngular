package fr.swymn.backend.service;

public class BadCredential extends RuntimeException {
    public BadCredential() {
        super("Username or password is incorrect");
    }
}
