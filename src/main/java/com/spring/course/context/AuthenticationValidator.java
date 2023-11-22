package com.spring.course.context;


import com.spring.course.exception.UserNotFoundException;
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
     * @return The authenticated User if available.
     * @throws UserNotFoundException if the user is not authenticated or if the authentication
     *         principal is not an instance of User.
     */
    public static User getAuthenticatedUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new UserNotFoundException("User not found, please include valid token in request header");
    }
}
