package com.example.demonew.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for hashing and verifying passwords using SHA-256.
 * Passwords are never stored in plaintext.
 */
public class PasswordUtil {

    private PasswordUtil() {}

    /** Returns the SHA-256 hex digest of the given plaintext. */
    public static String hash(String plaintext) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plaintext.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /** Returns true if plaintext matches the stored hash. */
    public static boolean matches(String plaintext, String storedHash) {
        return hash(plaintext).equals(storedHash);
    }
}
