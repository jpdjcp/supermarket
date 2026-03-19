package com.supermarket.supermarket_api.security.jwt;

import com.supermarket.supermarket_api.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // extract token from request header
        String authHeader = request.getHeader("Authorization");
        // if header null or auth header starts different from "Bearer_"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // pass through filters
            filterChain.doFilter(request, response);
            return;
        }
        // extract token from header
        String token = authHeader.substring(7);
        // extract username from token
        String username = jwtService.extractUsername(token);

        // if username present but not authenticated yet
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            // get user from DB
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);
            // if token.username equals to userDetails.username
            if (jwtService.isTokenValid(token, userDetails)) {
                // create authentication
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                // set authenticated in context
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }
        // do filters
        filterChain.doFilter(request, response);
    }
}
