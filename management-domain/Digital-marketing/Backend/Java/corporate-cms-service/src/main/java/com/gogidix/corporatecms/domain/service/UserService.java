package com.gogidix.corporatecms.domain.service;

import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.application.dto.UserDTO;
import com.gogidix.corporatecms.application.exception.DuplicateResourceException;
import com.gogidix.corporatecms.application.exception.ResourceNotFoundException;
import com.gogidix.corporatecms.application.exception.UnauthorizedException;
import com.gogidix.corporatecms.application.mapper.UserMapper;
import com.gogidix.corporatecms.application.security.UserDetailsImpl;
import com.gogidix.corporatecms.domain.enums.UserRole;
import com.gogidix.corporatecms.domain.model.User;
import com.gogidix.corporatecms.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createUser(UserDTO dto, String createdBy) {
        log.info("Creating user with username: {}", dto.getUsername());

        if (userRepository.existsByUsernameAndDeletedFalse(dto.getUsername())) {
            throw new DuplicateResourceException("User", "username", dto.getUsername());
        }

        if (userRepository.existsByEmailAndDeletedFalse(dto.getEmail())) {
            throw new DuplicateResourceException("User", "email", dto.getEmail());
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(generateTemporaryPassword()));
        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setRole(dto.getRole() != null ? dto.getRole() : UserRole.VIEWER);

        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());

        return userMapper.toDto(savedUser);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public UserDTO updateUser(String id, UserDTO dto) {
        log.info("Updating user: {}", id);

        if (!isAdmin() && !isCurrentUser(id)) {
            throw new UnauthorizedException("You don't have permission to update this user");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!user.getUsername().equals(dto.getUsername()) &&
                userRepository.existsByUsernameAndDeletedFalse(dto.getUsername())) {
            throw new DuplicateResourceException("User", "username", dto.getUsername());
        }

        if (!user.getEmail().equals(dto.getEmail()) &&
                userRepository.existsByEmailAndDeletedFalse(dto.getEmail())) {
            throw new DuplicateResourceException("User", "email", dto.getEmail());
        }

        userMapper.updateEntityFromDto(dto, user);
        User savedUser = userRepository.save(user);

        log.info("User updated: {}", id);
        return userMapper.toDto(savedUser);
    }

    @Cacheable(value = "users", key = "#id")
    public UserDTO getUserById(String id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toDto(user);
    }

    public UserDTO getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return userMapper.toDto(user);
    }

    public PageResponse<UserDTO> getUsersByRole(UserRole role, int page, int size) {
        log.info("Fetching users by role: {}", role);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username"));
        Page<User> userPage = userRepository.findByRoleAndDeletedFalse(role, pageable);
        return PageResponse.of(userPage.map(userMapper::toDto));
    }

    public PageResponse<UserDTO> searchUsers(String keyword, int page, int size) {
        log.info("Searching users with keyword: {}", keyword);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username"));
        Page<User> userPage = userRepository.searchByKeyword(keyword, pageable);
        return PageResponse.of(userPage.map(userMapper::toDto));
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(String id) {
        log.info("Deleting user: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!isAdmin() && !isCurrentUser(id)) {
            throw new UnauthorizedException("You don't have permission to delete this user");
        }

        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        user.setEnabled(false);

        userRepository.save(user);
        log.info("User deleted: {}", id);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public UserDTO changePassword(String id, String oldPassword, String newPassword) {
        log.info("Changing password for user: {}", id);

        if (!isCurrentUser(id)) {
            throw new UnauthorizedException("You can only change your own password");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UnauthorizedException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        User savedUser = userRepository.save(user);

        log.info("Password changed for user: {}", id);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public UserDTO updateLastLogin(String id, String ip) {
        log.info("Updating last login for user: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setLastLoginAt(LocalDateTime.now());
        user.setLastLoginIp(ip);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public List<UserDTO> getActiveUsers() {
        List<User> users = userRepository.findByEnabledTrueAndDeletedFalse();
        return userMapper.toDtoList(users);
    }

    public List<UserDTO> getUsersByDepartment(String department) {
        List<User> users = userRepository.findByDepartmentAndDeletedFalse(department);
        return userMapper.toDtoList(users);
    }

    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 12);
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a instanceof SimpleGrantedAuthority
                        && a.getAuthority().equals("ROLE_ADMIN"));
    }

    private boolean isCurrentUser(String userId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails.getId().equals(userId);
        }
        return false;
    }
}
