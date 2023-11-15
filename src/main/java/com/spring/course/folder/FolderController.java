package com.spring.course.folder;


import com.spring.course.exception.ResourceNotFoundException;
import com.spring.course.exception.UnauthorizedAccessException;
import com.spring.course.file.FileService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<FolderResponse> createFolder(@RequestBody FolderRequest folderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folderService.createFolder(folderRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<FolderResponse> getAllFoldersByUser() {
        return ResponseEntity.ok(folderService.getAllFoldersByUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderResponse> getFolderById(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.getFolderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FolderResponse> deleteFolderById(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.deleteFolderById(id));
    }
}
