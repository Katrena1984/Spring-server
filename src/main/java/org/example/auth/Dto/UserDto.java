package org.example.auth.Dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserDto {
    private String email;
    private String password;
    //private String role; // ADMIN, EDITOR, VIEWER
    @NotBlank
    private String username;
}
