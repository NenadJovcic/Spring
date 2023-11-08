package com.spring.course.controller;

import com.spring.course.entity.FileEntity;
import com.spring.course.entity.Folder;
import com.spring.course.exception.ResourceNotFoundException;
import com.spring.course.exception.UnauthorizedAccessException;
import com.spring.course.exception.UserNotFoundException;
import com.spring.course.repository.FileRepository;
import com.spring.course.repository.FolderRepository;
import com.spring.course.response.ApiResponse;
import com.spring.course.response.FileResponse;
import com.spring.course.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FileRepository fileRepository;


    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folderId") Long folderId) {
        try {
            fileService.uploadFile(file, folderId);
            return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (UnauthorizedAccessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFileById(@PathVariable Long id) {
        try {
            FileResponse downloadFileResponse = fileService.downloadFileById(id);
            ByteArrayResource resource = new ByteArrayResource(downloadFileResponse.getFileContent());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", downloadFileResponse.getFileName());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    FileResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    FileResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    FileResponse.builder()
                            .message("Something went wrong: \n" + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FileResponse> removeFileById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(fileService.removeFileById(id));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    FileResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    FileResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    FileResponse.builder()
                            .message("Internal Server Error")
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> getFileById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(fileService.getFileById(id));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    FileResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    FileResponse.builder()
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    FileResponse.builder()
                            .message("Internal Server Error")
                            .build());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<FileResponse> getAllFilesByUser() {
        try {
            return ResponseEntity.ok(fileService.getAllFilesByUser());
        } catch (UserNotFoundException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    FileResponse.builder()
                            .message(e.getMessage())
                            .build());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    FileResponse.builder()
                            .message("Internal Server Error")
                            .build());
        }
    }
}
