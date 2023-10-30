package com.spring.course.service;

import com.spring.course.entity.User;
import com.spring.course.repository.UserRepository;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

// All logik hanteras i service!
@Service
public class UserService {

    public UserRepository userRepository;


    public Optional<User> saveUser(User user) {
       return userRepository.findByUsername(user.getUsername());
    }
}
