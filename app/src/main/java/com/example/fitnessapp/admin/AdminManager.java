package com.example.fitnessapp.admin;

import android.content.Context;
import android.content.SharedPreferences;

public class AdminManager {
    private static final String PREF_NAME = "AdminPrefs";
    private static final String KEY_ADMIN_EMAIL = "admin_email";
    private static final String KEY_ADMIN_PASSWORD = "admin_password";
    private static final String KEY_IS_ADMIN_REGISTERED = "is_admin_registered";

    private SharedPreferences sharedPreferences;

    public AdminManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean registerAdmin(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return false;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ADMIN_EMAIL, email);
        editor.putString(KEY_ADMIN_PASSWORD, password);
        editor.putBoolean(KEY_IS_ADMIN_REGISTERED, true);
        return editor.commit();
    }

    public boolean isAdmin(String email, String password) {
        if (!isAdminRegistered()) {
            return false;
        }

        String savedEmail = sharedPreferences.getString(KEY_ADMIN_EMAIL, "");
        String savedPassword = sharedPreferences.getString(KEY_ADMIN_PASSWORD, "");

        return savedEmail.equals(email) && savedPassword.equals(password);
    }

    public boolean isAdminRegistered() {
        return sharedPreferences.getBoolean(KEY_IS_ADMIN_REGISTERED, false);
    }

    public String getAdminEmail() {
        return sharedPreferences.getString(KEY_ADMIN_EMAIL, "");
    }
}