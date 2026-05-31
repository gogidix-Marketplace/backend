package com.gogidix.corporatecms.domain.service;

import com.gogidix.corporatecms.application.dto.UserDTO;
import com.gogidix.corporatecms.application.exception.ResourceNotFoundException;
import com.gogidix.corporatecms.application.exception.UnauthorizedException;
import com.gogidix.corporatecms.application.mapper.UserMapper;
import com.gogidix.corporatecms.application.security.JwtTokenProvider;
import com.gogidix.corporatecms.application.security.UserDetailsImpl;
import com.gogidix.corporatecms.application.security.UserDetailsServiceImpl;
import com.gogidix.corporatecms.domain.model.User;
import com.gogidix.corporatecms.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    public Map<String, Object> authenticate(String usernameOrEmail, String password, String ip) {
        log.info("Authentication attempt for: {}", usernameOrEmail);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameOrEmail, password)
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String token = tokenProvider.generateToken(userDetails.getId(), userDetails.getUsername());
            String refreshToken = tokenProvider.generateRefreshToken(userDetails.getId(), userDetails.getUsername());

            User user = userRepository.findById(userDetails.getId()).orElseThrow();

            userService.updateLastLogin(userDetails.getId(), ip);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("refreshToken", refreshToken);
            response.put("tokenType", "Bearer");
            response.put("expiresIn", tokenProvider.getExpirationTime());
            response.put("user", userMapper.toDto(user));

            log.info("Authentication successful for: {}", usernameOrEmail);
            return response;

        } catch (Exception e) {
            log.error("Authentication failed for: {}", usernameOrEmail);
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    public Map<String, Object> refreshToken(String refreshToken) {
        log.info("Token refresh attempt");

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String userId = tokenProvider.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!user.getEnabled()) {
            throw new UnauthorizedException("User account is disabled");
        }

        String newToken = tokenProvider.generateToken(user.getId(), user.getUsername());
        String newRefreshToken = tokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("token", newToken);
        response.put("refreshToken", newRefreshToken);
        response.put("tokenType", "Bearer");
        response.put("expiresIn", tokenProvider.getExpirationTime());

        log.info("Token refreshed for user: {}", user.getUsername());
        return response;
    }

    public UserDTO getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return userMapper.toDto(user);
    }
}
