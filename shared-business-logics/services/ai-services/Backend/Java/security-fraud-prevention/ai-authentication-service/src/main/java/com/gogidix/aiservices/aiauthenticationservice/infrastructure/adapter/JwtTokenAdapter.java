package com.gogidix.aiservices.aiauthenticationservice.infrastructure.adapter;

import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.TokenPolicy;
import com.gogidix.aiservices.aiauthenticationservice.infrastructure.config.JwtProperties;

import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JwtTokenAdapter implements TokenPolicy {
    private final JwtProperties properties;
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final HmacHelper hmacHelper;

    public JwtTokenAdapter(JwtProperties properties) {
        if (properties.getSecret() == null || properties.getSecret().length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters");
        }
        this.properties = properties;
        this.hmacHelper = new HmacHelper(properties.getSecret());
    }

    @Override
    public String generateAccessToken(String userId, Set<String> roles) {
        long now = System.currentTimeMillis();
        long exp = now + (properties.getAccessTokenExpiration() * 1000L);

        String header = Base64.getUrlEncoder().withoutPadding().encodeToString(
                ("{\"alg\":\"HS256\",\"typ\":\"JWT\"}").getBytes()
        );

        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                String.format("{\"sub\":\"%s\",\"roles\":\"%s\",\"iss\":\"%s\",\"iat\":%d,\"exp\":%d}",
                        userId, String.join(",", roles), properties.getIssuer(), now / 1000, exp / 1000).getBytes()
        );

        String signature = generateSignature(header, payload);

        return header + "." + payload + "." + signature;
    }

    @Override
    public String generateRefreshToken(String userId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String raw = userId + "-" + System.currentTimeMillis() + "-" + UUID.randomUUID();
            byte[] hash = digest.digest(raw.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    @Override
    public boolean validateAccessToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        if (isTokenBlacklisted(token)) {
            return false;
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        try {
            String expectedSignature = generateSignature(parts[0], parts[1]);
            return expectedSignature.equals(parts[2]) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateRefreshToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return token.matches("[0-9a-f]{64}");
    }

    @Override
    public String extractUserId(String token) {
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
        return payload.substring(6, payload.indexOf("\",\"roles\""));
    }

    @Override
    public Set<String> extractRoles(String token) {
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
        int rolesStart = payload.indexOf("\"roles\":\"") + 9;
        int rolesEnd = payload.indexOf("\",\"iss\"", rolesStart);
        String rolesStr = payload.substring(rolesStart, rolesEnd);
        return Set.of(rolesStr.split(","));
    }

    @Override
    public boolean isTokenExpired(String token) {
        Long exp = extractExpiration(token);
        return exp != null && exp * 1000 < System.currentTimeMillis();
    }

    @Override
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    @Override
    public int getAccessTokenExpiration() {
        return properties.getAccessTokenExpiration();
    }

    @Override
    public int getRefreshTokenExpiration() {
        return properties.getRefreshTokenExpiration();
    }

    @Override
    public Long extractExpiration(String token) {
        try {
            String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            int expStart = payload.indexOf("\"exp\":") + 6;
            int expEnd = payload.indexOf("}", expStart);
            return Long.parseLong(payload.substring(expStart, expEnd));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Long extractIssuedAt(String token) {
        try {
            String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            int iatStart = payload.indexOf("\"iat\":") + 6;
            int iatEnd = payload.indexOf(",\"exp\"", iatStart);
            return Long.parseLong(payload.substring(iatStart, iatEnd));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String extractIssuer(String token) {
        try {
            String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            int issStart = payload.indexOf("\"iss\":\"") + 7;
            int issEnd = payload.indexOf("\",\"iat\"", issStart);
            return payload.substring(issStart, issEnd);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    private String generateSignature(String header, String payload) {
        String data = header + "." + payload;
        byte[] hash = hmacHelper.hmac(properties.getSecret().getBytes(), data.getBytes());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    private static class HmacHelper {
        private final byte[] secretKey;

        HmacHelper(String secretKey) {
            this.secretKey = secretKey.getBytes();
        }

        byte[] hmac(byte[] key, byte[] data) {
            try {
                javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
                javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256");
                mac.init(secretKeySpec);
                return mac.doFinal(data);
            } catch (Exception e) {
                throw new RuntimeException("HMAC calculation failed", e);
            }
        }
    }
}
