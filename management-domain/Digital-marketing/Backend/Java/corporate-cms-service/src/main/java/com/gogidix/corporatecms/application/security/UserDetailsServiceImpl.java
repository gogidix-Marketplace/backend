package com.gogidix.corporatecms.application.security;

import com.gogidix.corporatecms.domain.model.User;
import com.gogidix.corporatecms.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserDetailsService for Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmailAndDeletedFalse(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username or email: " + username));

        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .authorities(user.getRole().getAuthorities())
                .enabled(user.getEnabled())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + id));

        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .authorities(user.getRole().getAuthorities())
                .enabled(user.getEnabled())
                .build();
    }
}
