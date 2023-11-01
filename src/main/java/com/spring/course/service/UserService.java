package com.spring.course.service;

import com.spring.course.config.JwtService;
import com.spring.course.entity.User;
import com.spring.course.entity.UserRole;
import com.spring.course.repository.UserRepository;
import com.spring.course.request.AuthenticationRequest;
import com.spring.course.request.RegisterRequest;
import com.spring.course.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

// All logik hanteras i service!
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
