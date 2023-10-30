package com.spring.course.controller;

import com.spring.course.entity.User;
import com.spring.course.repository.UserRepository;
import com.spring.course.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;
    private UserRepository userRepository;


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(savedUser -> ResponseEntity.ok().body(savedUser))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/register")
    public ResponseEntity<Optional<User>> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Optional<User> savedUser = userService.saveUser(user); // Använd userService för att spara användaren
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return ResponseEntity.ok("Inloggning lyckades.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ogiltigt användarnamn eller lösenord.");
        }
    }
}
