package com.stream.authservice.auth;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerateSecretKey {
    public static void main(String[] args) {
        byte[] key = new byte[32]; // 256 bits
        new SecureRandom().nextBytes(key);
        String base64Key = Base64.getEncoder().encodeToString(key);
        System.out.println("Your secure base64 key: " + base64Key);
    }
}
