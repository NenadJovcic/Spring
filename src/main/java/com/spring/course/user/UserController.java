package com.spring.course.user;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller class handling user-related operations such as registration and authentication.
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity with the user information and HTTP status OK, or NOT_FOUND if the user is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(savedUser -> ResponseEntity.ok().body(savedUser))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details.
     * @return ResponseEntity with the authentication response and HTTP status OK, or BAD_REQUEST if an error occurred.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
        AuthenticationResponse registrationResponse = userService.register(request);
        if (registrationResponse.isErrorOccurred()) return ResponseEntity.badRequest().body(registrationResponse);

        return ResponseEntity.ok(registrationResponse);
    }

    /**
     * Authenticates a user.
     *
     * @param request  The authentication request containing user credentials.
     * @param response The HTTP servlet response.
     * @return ResponseEntity with the authentication response and HTTP status OK.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.authenticate(request, response));
    }
}
