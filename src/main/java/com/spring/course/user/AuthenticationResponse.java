package com.spring.course.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Response class for authentication containing a JWT token and additional information.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private String token;
    private String message;
    private boolean errorOccurred;
}
