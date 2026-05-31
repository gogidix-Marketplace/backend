package com.gogidix.corporatecms.application.security;

import com.gogidix.corporatecms.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of Spring Security UserDetails.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled != null && enabled;
    }

    public static UserDetailsImpl build(String id, String username, String email,
                                        String password, UserRole role, Boolean enabled) {
        List<SimpleGrantedAuthority> authorities = role.getAuthorities().stream().toList();

        return UserDetailsImpl.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .authorities(authorities)
                .enabled(enabled)
                .build();
    }
}
