package com.gogidix.aiservices.aiauthenticationservice.domain.port.out;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
