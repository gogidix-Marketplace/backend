package com.gogidix.management.shared.infrastructure.security;

public interface JwtTokenValidator {
    java.util.Map<String, Object> validate(String token) throws Exception;
}
