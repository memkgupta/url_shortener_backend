package com.url_shortner.shortner.config;

public class Base62Encoder {
    private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;

    public static String encode(long number) {
        StringBuilder encoded = new StringBuilder();
        while (number > 0) {
            encoded.insert(0, BASE62_ALPHABET.charAt((int) (number % BASE)));
            number /= BASE;
        }
        return encoded.toString();
    }

    public static long decode(String base62) {
        long result = 0;
        for (char c : base62.toCharArray()) {
            result = result * BASE + BASE62_ALPHABET.indexOf(c);
        }
        return result;
    }
}
