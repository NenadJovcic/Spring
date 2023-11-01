package com.spring.course.controller;

import com.spring.course.entity.Folder;
import com.spring.course.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/folders")
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<Object> createFolder(@RequestBody Folder folder) {
        try {
            Folder createdFolder = folderService.createFolder(folder);
            return new ResponseEntity<>(createdFolder, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Failed to create folder: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
