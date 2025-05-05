package com.chiro.util;

// Helper class for services
public class ServiceValidationHelper {
    public static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
    }

    public static void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
    }

    public static void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " cannot be longer than " + maxLength + " characters.");
        }
    }

    // Regex: allows for phone numbers in format (###)###-#####, ##########, ###-###-#####
    public static void validatePhoneNumberFormat(String phoneNumber) {
        if(phoneNumber != null && !phoneNumber.matches("/^\\(?([0-9]{3})\\)?[-.●]?([0-9]{3})[-.●]?([0-9]{4})$\n" + "/gm")) {
            throw new IllegalArgumentException(phoneNumber + "Invalid phone number format: " + phoneNumber);

        }
    }

    // Regex: any amount of characters followed by @ followed by any amount of characters followed by .
    // followed by 3 characters
    public static void validateEmailFormat(String email) {
        if (email != null && !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{3,}$")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
}
