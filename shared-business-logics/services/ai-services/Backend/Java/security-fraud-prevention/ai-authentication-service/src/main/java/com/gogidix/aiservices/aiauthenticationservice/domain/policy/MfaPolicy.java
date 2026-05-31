package com.gogidix.aiservices.aiauthenticationservice.domain.policy;

import com.gogidix.aiservices.aiauthenticationservice.domain.model.AuthenticationResult;

import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MfaPolicy {
    private static final int MFA_CODE_LENGTH = 6;
    private static final int MFA_EXPIRY_SECONDS = 300;
    private static final int MAX_MFA_ATTEMPTS = 3;
    private static final double MFA_RISK_THRESHOLD = 0.4;

    private final Map<String, MfaCodeData> activeCodes = new ConcurrentHashMap<>();
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public boolean isMfaRequired(AuthenticationResult result) {
        return result.getRiskScore() != null && result.getRiskScore() >= MFA_RISK_THRESHOLD ||
               result.getRoles() != null && result.getRoles().contains("ADMIN");
    }

    public String generateMfaCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < MFA_CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        String codeStr = code.toString();
        activeCodes.put(codeStr, new MfaCodeData(codeStr, System.currentTimeMillis() + MFA_EXPIRY_SECONDS * 1000));
        return codeStr;
    }

    public boolean validateMfaCode(String storedCode, String providedCode) {
        MfaCodeData data = activeCodes.get(storedCode);
        if (data == null) {
            return false;
        }

        if (System.currentTimeMillis() > data.expiryTime) {
            activeCodes.remove(storedCode);
            return false;
        }

        if (data.code.equals(providedCode)) {
            failedAttempts.remove(storedCode);
            return true;
        }

        failedAttempts.merge(storedCode, 1, Integer::sum);
        return false;
    }

    public void expireCode(String code) {
        activeCodes.remove(code);
    }

    public boolean isMfaLocked(String code) {
        return failedAttempts.getOrDefault(code, 0) >= MAX_MFA_ATTEMPTS;
    }

    public String getPreferredMethod(java.util.Set<String> availableMethods) {
        if (availableMethods.contains("TOTP")) return "TOTP";
        if (availableMethods.contains("SMS")) return "SMS";
        return "EMAIL";
    }

    private record MfaCodeData(String code, long expiryTime) {}
}
