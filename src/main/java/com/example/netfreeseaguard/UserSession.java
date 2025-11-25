package com.example.netfreeseaguard;

public class UserSession {

    // "visser" or "beheerder"
    private static String currentRole = "visser";

    // simple in-memory registered user (only one, for demo)
    private static String registeredEmail;
    private static String registeredPassword;

    public static String getCurrentRole() {
        return currentRole;
    }

    public static void setCurrentRole(String role) {
        if (role == null || role.isBlank()) {
            currentRole = "visser";
        } else {
            currentRole = role;
        }
        System.out.println("UserSession role set to: " + currentRole);
    }

    public static void registerUser(String email, String password) {
        registeredEmail = email;
        registeredPassword = password;
        System.out.println("Registered user: " + registeredEmail);
    }

    public static boolean matchesRegisteredUser(String email, String password) {
        return registeredEmail != null
                && registeredPassword != null
                && registeredEmail.equals(email)
                && registeredPassword.equals(password);
    }
}
