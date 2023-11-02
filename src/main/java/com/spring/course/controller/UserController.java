package com.spring.course.controller;

import com.spring.course.entity.User;
import com.spring.course.repository.UserRepository;
import com.spring.course.request.AuthenticationRequest;
import com.spring.course.request.RegisterRequest;
import com.spring.course.response.AuthenticationResponse;
import com.spring.course.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(savedUser -> ResponseEntity.ok().body(savedUser))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
        AuthenticationResponse registrationResponse = userService.register(request);
        if (registrationResponse.isErrorOccurred()) return ResponseEntity.badRequest().body(registrationResponse);

        return ResponseEntity.ok(registrationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(userService.authenticate(request, response));
    }
}
