package com.spring.course.controller;

import com.spring.course.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @PathVariable Long folderId) {
        String fileName = fileService.uploadFile(file, folderId);
        return new ResponseEntity<>("File uploaded successfully: " + fileName, HttpStatus.OK);
    }
}
