package com.stream.authservice.config;

import com.stream.authservice.auth.JwtService;
import com.stream.authservice.user.Role;
import com.stream.authservice.user.User;
import com.stream.authservice.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // Kick user attributes example - adjust if Kick returns differently
        String id = oauthUser.getAttribute("id");
        String username = oauthUser.getAttribute("username");
        String email = oauthUser.getAttribute("email");  // Kick may not provide email, check API
        String firstName = oauthUser.getAttribute("first_name");
        String lastName = oauthUser.getAttribute("last_name");

        if (email == null || email.isBlank()) {
            // fallback or assign dummy email if Kick API doesn't return email
            email = (username != null ? username : "unknown") + "@kickuser.local";
        }

        final String finalEmail = email;
        final String finalFirstName = firstName;
        final String finalLastName = lastName;
        final String finalUsername = username;

        User user = userRepository.findByEmail(finalEmail).orElseGet(() -> {
            User newUser = User.builder()
                    .email(finalEmail)
                    .firstname(finalFirstName != null ? finalFirstName : finalUsername)
                    .lastname(finalLastName != null ? finalLastName : "")
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .role(Role.USER)
                    .build();
            return userRepository.save(newUser);
        });




        String jwtToken = jwtService.generateToken(user);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonResponse = "{\"token\":\"" + jwtToken + "\"}";
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
