package com.spring.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.course.user.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registerUser_Success() throws Exception {
        // Mocking the registration response
        RegisterRequest registerRequest = new RegisterRequest("testUser", "test@example.com", "password");
        AuthenticationResponse registrationResponse = AuthenticationResponse.builder()
                .errorOccurred(false)
                .token("mockedToken")
                .message("User created")
                .build();

        // Mocking user repository behavior
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());

        // Mocking user service behavior
        Mockito.when(userService.register(Mockito.any(RegisterRequest.class))).thenReturn(registrationResponse);

        // Perform the registration request
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // Validate the response
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorOccurred").value(false))
                .andExpect(jsonPath("$.token").value("mockedToken"))
                .andExpect(jsonPath("$.message").value("User created"));
    }

    @Test
    public void registerUser_EmailAlreadyExists() throws Exception {
        // Mocking the registration response
        RegisterRequest registerRequest = new RegisterRequest("testUser", "test@example.com", "password");
        AuthenticationResponse registrationResponse = AuthenticationResponse.builder()
                .errorOccurred(true)
                .token(null)
                .message("There is already a user with this email")
                .build();

        // Mocking user repository behavior
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(new User()));

        // Mocking user service behavior
        Mockito.when(userService.register(Mockito.any(RegisterRequest.class))).thenReturn(registrationResponse);

        // Perform the registration request
        ResultActions result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // Validate the response
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorOccurred").value(true))
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(jsonPath("$.message").value("There is already a user with this email"));
    }
}
