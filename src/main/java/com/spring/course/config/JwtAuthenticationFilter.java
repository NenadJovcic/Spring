package com.spring.course.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // This method is called for each incoming HTTP request to perform authentication.
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Extract the "Authorization" header from the HTTP request.
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if (isPermittedRoute(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check if the header is missing or doesn't start with "Bearer ".
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            handleException(response, "Missing or incorrect Authorization header. No Bearer token found.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            // If the header is valid, extract the JWT (JSON Web Token) from it (excluding "Bearer ").
            jwtToken = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwtToken);

            // Check if the user is not already authenticated.
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details from the userDetailsService.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Check if the JWT token is valid for the user.
                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    // Create an authentication token for the user.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set the authentication token in the SecurityContextHolder.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Continue with the next filter in the chain.
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            handleException(response, "Token expired", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException e) {
            handleException(response, "Invalid token", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (AuthenticationServiceException e) {
            handleException(response, e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void handleException(HttpServletResponse response, String errorMessage, int httpStatus) throws IOException {
        response.setStatus(httpStatus);
        response.getWriter().write("Authentication failed: " + errorMessage);
        response.getWriter().flush();
    }

    private boolean isPermittedRoute(String requestURI) {
        for (String permittedRoute : SecurityConfiguration.PERMITTED_ROUTES) {
            if (requestURI.equals(permittedRoute) || requestURI.startsWith(permittedRoute + "/")) {
                return true;
            }
        }
        return false;
    }
}