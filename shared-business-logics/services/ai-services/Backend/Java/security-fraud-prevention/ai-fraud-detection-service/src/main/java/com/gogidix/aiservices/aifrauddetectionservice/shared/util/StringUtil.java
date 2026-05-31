package com.gogidix.aiservices.aifrauddetectionservice.shared.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for string operations.
 * <p>
 * Provides methods for string validation, manipulation, and ID generation.
 */
@Slf4j
@UtilityClass
public class StringUtil {

    /**
     * Secure random instance for ID generation.
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Pattern for detecting SQL injection attempts.
     */
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            "(?i).*(union|select|insert|update|delete|drop|create|alter|exec|execute|script|javascript|eval).*"
    );

    /**
     * Pattern for detecting XSS attempts.
     */
    private static final Pattern XSS_PATTERN = Pattern.compile(
            "(?i).*(<script|</script|javascript:|onerror|onload|onclick|onmouseover).*"
    );

    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty
     */
    public boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a string is null, empty, or blank.
     *
     * @param str the string to check
     * @return true if the string is null, empty, or blank
     */
    public boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks if a string is not empty.
     *
     * @param str the string to check
     * @return true if the string is not empty
     */
    public boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * Checks if a string is not blank.
     *
     * @param str the string to check
     * @return true if the string is not blank
     */
    public boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Truncates a string to a maximum length, appending ellipsis if needed.
     *
     * @param str    the string to truncate
     * @param maxLength the maximum length
     * @return the truncated string, or the original if shorter
     */
    public String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Truncates a string to a maximum length without appending ellipsis.
     *
     * @param str        the string to truncate
     * @param maxLength  the maximum length
     * @return the truncated string
     */
    public String truncateSilent(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }

    /**
     * Generates a random UUID string.
     *
     * @return a random UUID string
     */
    public String generateUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a random ID string of specified length.
     *
     * @param length the length of the ID to generate
     * @return a random alphanumeric ID string
     */
    public String generateId(int length) {
        if (length <= 0) {
            return "";
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(SECURE_RANDOM.nextInt(chars.length())));
        }

        return sb.toString();
    }

    /**
     * Generates a secure random token using Base64 encoding.
     *
     * @param byteLength the number of random bytes
     * @return a Base64-encoded random token
     */
    public String generateSecureToken(int byteLength) {
        byte[] bytes = new byte[byteLength];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Generates a numeric ID string of specified length.
     *
     * @param length the length of the numeric ID
     * @return a random numeric ID string
     */
    public String generateNumericId(int length) {
        if (length <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(SECURE_RANDOM.nextInt(10));
        }

        return sb.toString();
    }

    /**
     * Masks a string by replacing characters with asterisks.
     *
     * @param str            the string to mask
     * @param visibleChars   the number of characters to keep visible at the start
     * @param endChars       the number of characters to keep visible at the end
     * @return the masked string
     */
    public String mask(String str, int visibleChars, int endChars) {
        if (str == null || str.length() <= visibleChars + endChars) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(str, 0, visibleChars);

        for (int i = visibleChars; i < str.length() - endChars; i++) {
            sb.append('*');
        }

        sb.append(str.substring(str.length() - endChars));
        return sb.toString();
    }

    /**
     * Masks an email address, showing only the first character and domain.
     *
     * @param email the email to mask
     * @return the masked email (e.g., j***@example.com)
     */
    public String maskEmail(String email) {
        if (isBlank(email)) {
            return email;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return mask(email, 2, 0);
        }

        String local = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (local.length() <= 1) {
            return "*" + domain;
        }

        return local.charAt(0) + "****" + domain;
    }

    /**
     * Sanitizes a string to prevent injection attacks.
     *
     * @param str the string to sanitize
     * @return the sanitized string
     */
    public String sanitize(String str) {
        if (isBlank(str)) {
            return str;
        }

        return str
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("/", "&#x2F;");
    }

    /**
     * Checks if a string contains potential SQL injection patterns.
     *
     * @param str the string to check
     * @return true if potential SQL injection is detected
     */
    public boolean containsSqlInjection(String str) {
        return isNotBlank(str) && SQL_INJECTION_PATTERN.matcher(str).matches();
    }

    /**
     * Checks if a string contains potential XSS patterns.
     *
     * @param str the string to check
     * @return true if potential XSS is detected
     */
    public boolean containsXss(String str) {
        return isNotBlank(str) && XSS_PATTERN.matcher(str).matches();
    }

    /**
     * Joins a collection of strings with a separator.
     *
     * @param strings   the strings to join
     * @param separator the separator
     * @return the joined string
     */
    public String join(Collection<String> strings, String separator) {
        if (strings == null || strings.isEmpty()) {
            return "";
        }

        return String.join(separator, strings);
    }

    /**
     * Joins an array of strings with a separator.
     *
     * @param strings   the strings to join
     * @param separator the separator
     * @return the joined string
     */
    public String join(String[] strings, String separator) {
        if (strings == null || strings.length == 0) {
            return "";
        }

        return String.join(separator, strings);
    }

    /**
     * Converts a string to null if it's empty or blank.
     *
     * @param str the string to convert
     * @return the string, or null if empty or blank
     */
    public String emptyToNull(String str) {
        return isBlank(str) ? null : str;
    }

    /**
     * Converts a null string to an empty string.
     *
     * @param str the string to convert
     * @return the string, or empty string if null
     */
    public String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    /**
     * Reverses a string.
     *
     * @param str the string to reverse
     * @return the reversed string
     */
    public String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Counts the occurrences of a substring in a string.
     *
     * @param str       the string to search in
     * @param substring the substring to count
     * @return the number of occurrences
     */
    public int countOccurrences(String str, String substring) {
        if (isEmpty(str) || isEmpty(substring)) {
            return 0;
        }

        int count = 0;
        int index = 0;

        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }

        return count;
    }

    /**
     * Removes all whitespace from a string.
     *
     * @param str the string to process
     * @return the string without whitespace
     */
    public String removeWhitespace(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * Normalizes a string by trimming and converting to lowercase.
     *
     * @param str the string to normalize
     * @return the normalized string
     */
    public String normalize(String str) {
        if (str == null) {
            return null;
        }
        return str.trim().toLowerCase();
    }

    /**
     * Checks if two strings are equal, ignoring case.
     *
     * @param str1 the first string
     * @param str2 the second string
     * @return true if the strings are equal, ignoring case
     */
    public boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * Converts a string representation of a boolean to a Boolean.
     *
     * @param str the string to convert
     * @return the Boolean value, or null if invalid
     */
    public Boolean parseBoolean(String str) {
        if (isBlank(str)) {
            return null;
        }
        String lower = str.trim().toLowerCase();
        if ("true".equals(lower) || "yes".equals(lower) || "1".equals(lower) || "y".equals(lower)) {
            return true;
        }
        if ("false".equals(lower) || "no".equals(lower) || "0".equals(lower) || "n".equals(lower)) {
            return false;
        }
        return null;
    }
}
