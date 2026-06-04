package org.example.auth.config;

import lombok.RequiredArgsConstructor;
import org.example.auth.entity.RegistrationCode;
import org.example.auth.entity.User;
import org.example.auth.repository.RegistrationCodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RegistrationCodeRepository codeRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            System.out.println("Initializing registration codes...");
            
            ensureCodeExists("ADMIN-2024", User.Role.ADMIN);
            ensureCodeExists("EDITOR-2024", User.Role.EDITOR);
            ensureCodeExists("VIEWER-2024", User.Role.VIEWER);
            
            System.out.println("Registration codes ready!");
        };
    }

    private void ensureCodeExists(String codeValue, User.Role role) {
        if (!codeRepository.findByCode(codeValue).isPresent()) {
            RegistrationCode newCode = new RegistrationCode();
            newCode.setCode(codeValue);
            newCode.setRole(role);
            newCode.setUsed(false);
            codeRepository.save(newCode);
            System.out.println(" Created code: " + codeValue + " with role: " + role);
        } else {
            System.out.println("Code already exists: " + codeValue);
        }
    }
}
