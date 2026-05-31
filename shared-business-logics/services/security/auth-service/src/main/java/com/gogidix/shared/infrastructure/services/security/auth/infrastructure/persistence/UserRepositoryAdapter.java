package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter for UserRepository port.
 * <p>
 * Implements the domain port using Spring Data MongoDB.
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByIdAndTenantId(String userId, String tenantId) {
        return userRepository.findByIdAndTenantId_Value(userId, tenantId);
    }

    @Override
    public Optional<User> findByUsernameAndTenantId(String username, String tenantId) {
        return userRepository.findByUsernameAndTenantId_Value(username, tenantId);
    }

    @Override
    public Optional<User> findByEmailAndTenantId(String email, String tenantId) {
        return userRepository.findByEmailAndTenantId_Value(email, tenantId);
    }

    @Override
    public boolean existsByUsernameAndTenantId(String username, String tenantId) {
        return userRepository.existsByUsernameAndTenantId_Value(username, tenantId);
    }

    @Override
    public boolean existsByEmailAndTenantId(String email, String tenantId) {
        return userRepository.existsByEmailAndTenantId_Value(email, tenantId);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}
