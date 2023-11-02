package com.spring.course.controller;


import com.spring.course.entity.FileEntity;
import com.spring.course.entity.Folder;
import com.spring.course.repository.FolderRepository;
import com.spring.course.request.FolderRequest;
import com.spring.course.response.FolderResponse;
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
    private FolderRepository folderRepository;

    @PostMapping("/create")
    public ResponseEntity<FolderResponse> createFolder(@Valid @RequestBody FolderRequest folderRequest) {
        try {
            FolderResponse response = folderService.createFolder(folderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            String errorMessage = "Failed to create folder: " + e.getMessage();
            return ResponseEntity.badRequest().body(FolderResponse.builder()
                    .errorOccurred(true)
                    .message(errorMessage).build());
        }
    }
    @GetMapping("/{folderId}")
    public ResponseEntity<List<FileEntity>> getFilesInFolder(@PathVariable Long folderId) {
        Optional<Folder> optionalFolder = folderRepository.findById(folderId);

        if (optionalFolder.isPresent()) {
            Folder folder = optionalFolder.get();

            List<FileEntity> files = folder.getFiles();

            return new ResponseEntity<>(files, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
