package com.example.simulator.user.DTOs;

public record RegisterRequest(
        String firstname,
        String lastname,
        String email,
        String password) {}
