package org.example.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.entity.User;
import org.example.auth.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (email == null || email.isEmpty()) {
            log.error(" Email not provided by {}", registrationId);
            throw new OAuth2AuthenticationException("Email not provided by " + registrationId);
        }

        log.info("OAuth login: email={}, provider={}", email, registrationId);

        userRepository.findByEmail(email).ifPresentOrElse(
                user -> {
                    // Обновляем данные существующего пользователя
                    log.info("Updating existing user: {}", email);

                    String givenName = (String) attributes.get("given_name");
                    String familyName = (String) attributes.get("family_name");
                    String picture = (String) attributes.get("picture");

                    if (givenName != null) user.setGivenName(givenName);
                    if (familyName != null) user.setFamilyName(familyName);
                    if (picture != null) user.setPicture(picture);

                    userRepository.save(user);
                    log.info(" Updated user ID: {}", user.getId());
                },
                () -> {
                    // Создаём нового пользователя
                    log.info("Creating new user: {}", email);

                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setGivenName((String) attributes.get("given_name"));
                    newUser.setFamilyName((String) attributes.get("family_name"));
                    newUser.setPicture((String) attributes.get("picture"));
                    newUser.setUsername((String) attributes.get("name"));
                    newUser.setPassword(UUID.randomUUID().toString());

                    //newUser.setRole(User.Role.VIEWER);

                    User saved = userRepository.save(newUser);
                    log.info(" Saved new user with ID: {}", saved.getId());
                }
        );

        return oAuth2User;
    }
}