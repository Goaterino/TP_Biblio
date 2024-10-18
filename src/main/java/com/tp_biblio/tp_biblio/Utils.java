package com.tp_biblio.tp_biblio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    private static final String salt = "VeryAnnoyingSalt:";

    public static String hashString(String input) {
        try {
            String string_to_hash = salt+input;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(string_to_hash.getBytes(StandardCharsets.UTF_8));
            // make it human-readable
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /*public static void main(String[] args) {
        System.out.println(hashString("very_weak_password"));
    }*/
}
