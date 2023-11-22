package com.spring.course.exception;


import com.spring.course.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

/**
 * Global exception handler for handling various exceptions and providing consistent API responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException, which is thrown when validation fails for method arguments.
     *
     * @param e The exception instance.
     * @return ResponseEntity with a custom ApiResponse containing the error message and HTTP status code.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .message((e.getBindingResult().getFieldError() != null &&
                                e.getBindingResult().getFieldError().getCode().equals("NotNull")) ?
                                e.getBindingResult().getFieldError().getDefaultMessage() :
                                "Validation error: " + e.getMessage())
                        .build());
    }

    /**
     * Handles ResourceNotFoundException and returns a response with a 404 status.
     *
     * @param e The ResourceNotFoundException instance.
     * @return ResponseEntity with a ApiResponse containing the error message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.builder()
                        .message(e.getMessage())
                        .build());
    }

    /**
     * Handles UnauthorizedAccessException and returns a response with a 401 status.
     *
     * @param e The UnauthorizedAccessException instance.
     * @return ResponseEntity with a ApiResponse containing the error message.
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.builder()
                        .message(e.getMessage())
                        .build());
    }

    /**
     * Handles UserNotFoundException and returns a response with a 404 status.
     *
     * @param e The UserNotFoundException instance.
     * @return ResponseEntity with a ApiResponse containing the error message.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.builder()
                        .message(e.getMessage())
                        .build());
    }

    /**
     * Handles MissingFileException and returns a response with a 400 status.
     *
     * @param e The MissingFileException instance.
     * @return ResponseEntity with a ApiResponse containing the error message.
     */
    @ExceptionHandler(MissingFileException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(MissingFileException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .message(e.getMessage())
                        .build());
    }

    /**
     * Handles MissingServletRequestParameterException and returns a response with a 400 status.
     *
     * @param ex The MissingServletRequestParameterException instance.
     * @return ResponseEntity with a ApiResponse containing the error message.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        System.out.println(name + " parameter is missing");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .message(name + " parameter is missing")
                        .build());
    }

    /**
     * Handles IllegalArgumentException and returns a response with a 400 status.
     *
     * @param e The IllegalArgumentException instance.
     * @return ResponseEntity with a ApiResponse containing the error message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.builder()
                        .message(e.getMessage())
                        .build());
    }

    /**
     * Handles IOException and returns a response with a 500 status.
     *
     * @param e The IOException instance.
     * @return ResponseEntity with a ApiResponse containing the error message.
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse> handleIoException(IOException e) {
        System.out.println(e.getMessage() + "IOException");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.builder()
                        .message("Failed to upload/download the file")
                        .build());
    }

    /**
     * Handles generic Exception and returns a response with a 500 status.
     *
     * @param e The generic Exception instance.
     * @return ResponseEntity with a ApiResponse containing the error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        System.out.println(e.getMessage() + "Exception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.builder()
                        .message("Internal Server Error")
                        .build());
    }
}
