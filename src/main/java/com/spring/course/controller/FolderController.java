package com.spring.course.controller;


import com.spring.course.context.AuthenticationValidator;
import com.spring.course.entity.FileEntity;
import com.spring.course.entity.Folder;
import com.spring.course.entity.User;
import com.spring.course.exception.ResourceNotFoundException;
import com.spring.course.exception.UnauthorizedAccessException;
import com.spring.course.repository.FolderRepository;
import com.spring.course.request.FolderRequest;
import com.spring.course.response.FolderResponse;
import com.spring.course.service.FileService;
import com.spring.course.service.FolderService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/folders")
@Validated
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private FileService fileService;
    @Autowired
    private FolderRepository folderRepository;

    @PostMapping("/create")
    public ResponseEntity<FolderResponse> createFolder(@Valid @RequestBody FolderRequest folderRequest) {
        try {
            FolderResponse response = folderService.createFolder(folderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            String errorMessage = "Failed to create folder: " + e.getMessage();
            return ResponseEntity.badRequest().body(
                    FolderResponse.builder()
                            .errorOccurred(true)
                            .message(errorMessage)
                            .build());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<FolderResponse> getAllFoldersByUser() {
        try {
            return ResponseEntity.ok(folderService.getAllFoldersByUser());

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    FolderResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    FolderResponse.builder()
                            .message("Internal Server Error")
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FolderResponse> deleteFolderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(folderService.deleteFolderById(id));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    FolderResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    FolderResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    FolderResponse.builder()
                            .message("Internal Server Error")
                            .build());
        }
    }
}
