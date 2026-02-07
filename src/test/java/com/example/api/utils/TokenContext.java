package com.example.api.utils;

/**
 * Thread-local context for API authentication tokens.
 * Used by shared step definitions to pass tokens between AuthApiSteps and domain-specific steps.
 */
public class TokenContext {
    private static ThreadLocal<String> userToken = new ThreadLocal<>();
    private static ThreadLocal<String> adminToken = new ThreadLocal<>();

    public static void setUserToken(String token) {
        userToken.set(token);
    }

    public static String getUserToken() {
        return userToken.get();
    }

    public static void setAdminToken(String token) {
        adminToken.set(token);
    }

    public static String getAdminToken() {
        return adminToken.get();
    }

    public static void clear() {
        userToken.remove();
        adminToken.remove();
    }
}
