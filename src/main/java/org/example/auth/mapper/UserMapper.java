package org.example.auth.mapper;
import org.example.auth.Dto.UserDto;
import org.example.auth.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setUsername(dto.getUsername());
        return user;
    }

    public UserDto toDto(User entity) {
        UserDto dto = new UserDto();
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole().name());
        dto.setUsername(entity.getUsername());
        return dto;
    }
}