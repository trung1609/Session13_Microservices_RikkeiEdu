package com.trung.productservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("X-User-Username");
        String rolesHeader = request.getHeader("X-User-Role");

        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().contains("/api/products")) {
            if (rolesHeader == null || !rolesHeader.contains("ROLE_ADMIN")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                String errorResponse = "{\"status\": \"error\", \"message\": \"Access denied. Admin role required.\"}";
                response.getWriter().write(errorResponse);
                return;
            }
        }

        List<GrantedAuthority> authorities = Arrays.stream(rolesHeader != null ? rolesHeader.split(",") : new String[0])
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.trim())
                .toList();

        if (username != null && rolesHeader != null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
