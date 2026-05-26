package org.example.auth.Dto;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsDto {
    private String email;
    private String password;
}
