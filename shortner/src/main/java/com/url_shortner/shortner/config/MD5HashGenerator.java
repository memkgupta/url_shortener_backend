package com.url_shortner.shortner.config;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5HashGenerator {
    public static String getMD5(String input) {
        try {
            // Create a MessageDigest instance with MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Compute the hash and get byte array
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array to hexadecimal string
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            // Pad with leading zeros to ensure 32-character length
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    }