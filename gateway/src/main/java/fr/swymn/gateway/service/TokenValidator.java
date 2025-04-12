package fr.swymn.gateway.service;

public interface TokenValidator {

    boolean isValid(String token);
}
