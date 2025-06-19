package com.stream.authservice.auth;

import com.example.simulator.user.DTOs.AuthenticationRequest;
import com.example.simulator.user.DTOs.AuthenticationResponse;
import com.example.simulator.user.DTOs.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/v1/auth")

public class AuthenticationController {
    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    @GetMapping("/protected-endpoint")
    public String protectedEndpoint(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return "Welcome to protected endpoint! Authenticated as: " + userDetails.getUsername();
    }
}
