package com.spring.course.context;


import com.spring.course.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for validating and retrieving information about the authenticated user.
 */
public class AuthenticationValidator {

    /**
     * Retrieves the authenticated user from the security context.
     *
     * @return The authenticated User or null if not authenticated.
     */
    public static User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }
}
