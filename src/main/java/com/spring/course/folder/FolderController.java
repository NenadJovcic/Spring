package com.spring.course.folder;

import com.spring.course.file.FileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Controller handling operations related to folders.
 * Manages HTTP endpoints for creating, retrieving, and deleting folders.
 */
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

    /**
     * Endpoint for creating a new folder.
     *
     * @param folderRequest The request body containing folder details.
     * @return ResponseEntity with the created folder and HTTP status code 201 (Created).
     */
    @PostMapping("/create")
    public ResponseEntity<FolderResponse> createFolder(@Valid @RequestBody FolderRequest folderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folderService.createFolder(folderRequest));
    }

    /**
     * Endpoint for retrieving all folders belonging to the authenticated user.
     *
     * @return ResponseEntity with the list of folders and HTTP status code 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<FolderResponse> getAllFoldersByUser() {
        return ResponseEntity.ok(folderService.getAllFoldersByUser());
    }

    /**
     * Endpoint for retrieving a folder by its ID.
     *
     * @param id The ID of the folder to be retrieved.
     * @return ResponseEntity with the requested folder and HTTP status code 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<FolderResponse> getFolderById(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.getFolderById(id));
    }

    /**
     * Endpoint for deleting a folder by its ID.
     *
     * @param id The ID of the folder to be deleted.
     * @return ResponseEntity with a message indicating the deletion success and HTTP status code 200 (OK).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<FolderResponse> deleteFolderById(@PathVariable Long id) {
        return ResponseEntity.ok(folderService.deleteFolderById(id));
    }
}
