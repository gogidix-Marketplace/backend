package com.gogidix.ecosystem.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT Authentication Filter for the Gogidix Ecosystem.
 * Processes JWT tokens from HTTP requests and sets up Spring Security context.
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String userId = jwtTokenProvider.getUserIdFromToken(jwt);
                String username = jwtTokenProvider.getUsernameFromToken(jwt);
                List<String> authorities = jwtTokenProvider.getAuthoritiesFromToken(jwt);

                List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UserPrincipal userPrincipal = new UserPrincipal();
                userPrincipal.setId(Long.parseLong(userId));
                userPrincipal.setUsername(username);
                userPrincipal.setAuthorities(grantedAuthorities);

                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, grantedAuthorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}