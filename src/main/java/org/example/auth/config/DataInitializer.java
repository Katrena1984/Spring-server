package org.example.auth.config;

import org.example.auth.entity.RegistrationCode;
import org.example.auth.entity.User;
import org.example.auth.repository.RegistrationCodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RegistrationCodeRepository codeRepository) {
        return args -> {
            if (codeRepository.count() == 0) {
                System.out.println("Creating initial registration codes...");

                RegistrationCode adminCode = new RegistrationCode();
                adminCode.setCode("ADMIN-KEY-2024");
                adminCode.setRole(User.Role.ADMIN);
                adminCode.setUsed(false);
                codeRepository.save(adminCode);

                RegistrationCode editorCode = new RegistrationCode();
                editorCode.setCode("EDITOR-KEY-2024");
                editorCode.setRole(User.Role.EDITOR);
                editorCode.setUsed(false);
                codeRepository.save(editorCode);

                RegistrationCode viewerCode = new RegistrationCode();
                viewerCode.setCode("VIEWER-KEY-2024");
                viewerCode.setRole(User.Role.VIEWER);
                viewerCode.setUsed(false);
                codeRepository.save(viewerCode);

                System.out.println("Created 3 registration codes!");
            }
        };
    }
}
