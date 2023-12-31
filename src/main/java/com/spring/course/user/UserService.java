package com.spring.course.user;

import com.spring.course.config.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details.
     * @return AuthenticationResponse indicating the registration status and a JWT token.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if a user with the same email already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return AuthenticationResponse.builder()
                    .errorOccurred(true)
                    .token(null)
                    .message("There is already a user with this email ")
                    .build();
        } else {
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .errorOccurred(false)
                    .token(jwtToken)
                    .message("User created ")
                    .build();
        }
    }

    /**
     * Authenticates a user.
     *
     * @param request  The authentication request containing user credentials.
     * @param response The HTTP servlet response.
     * @return AuthenticationResponse indicating the authentication status and a JWT token.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .errorOccurred(false)
                .message("Successfully logged in ")
                .token(jwtToken)
                .build();
    }
}
