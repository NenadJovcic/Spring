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
