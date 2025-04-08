package fr.swymn.backend.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserCredential {
    @NotEmpty(message = "Login is required.")
    private String login; 

    @Length(min = 8, message = "Password must be at least 8 characters long.")
    @NotEmpty(message = "Password is required.")
    private String password;
}
