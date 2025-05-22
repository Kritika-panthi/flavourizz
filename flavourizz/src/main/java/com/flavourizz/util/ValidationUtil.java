package com.flavourizz.util;

public class ValidationUtil {

    // Check if a String is null or empty after trimming
    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }

    // Check if two passwords match exactly
    public static boolean doPasswordsMatch(String pwd1, String pwd2) {
        if (pwd1 == null || pwd2 == null) {
            return false;
        }
        return pwd1.equals(pwd2);
    }
}
