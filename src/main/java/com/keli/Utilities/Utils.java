package com.keli.Utilities;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Utils {
    public static String encodeBasicAuth(String username, String password) {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
