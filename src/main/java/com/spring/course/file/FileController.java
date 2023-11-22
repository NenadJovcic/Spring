package com.spring.course.file;


import com.spring.course.folder.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FileRepository fileRepository;

    /**
     * Handles the upload of a file to a specified folder.
     *
     * @param file     The file to upload.
     * @param folderId The ID of the folder where the file will be uploaded.
     * @return ResponseEntity with a success message and HTTP status OK.
     * @throws IOException If an IO error occurs during file upload.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folderId") Long folderId) throws IOException {
        fileService.uploadFile(file, folderId);
        return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);

    }

    /**
     * Downloads a file by its ID.
     *
     * @param id The ID of the file to download.
     * @return ResponseEntity with the file content, headers, and HTTP status OK.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFileById(@PathVariable Long id) {

        FileResponse downloadFileResponse = fileService.downloadFileById(id);
        ByteArrayResource resource = new ByteArrayResource(downloadFileResponse.getFileContent());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", downloadFileResponse.getFileName());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * Removes a file by its ID.
     *
     * @param id The ID of the file to remove.
     * @return ResponseEntity with a success message and HTTP status OK.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<FileResponse> removeFileById(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.removeFileById(id));
    }

    /**
     * Retrieves a file by its ID.
     *
     * @param id The ID of the file to retrieve.
     * @return ResponseEntity with the file information and HTTP status OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> getFileById(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    /**
     * Retrieves all files associated with the authenticated user.
     *
     * @return ResponseEntity with the list of files and HTTP status OK.
     */
    @GetMapping("/all")
    public ResponseEntity<FileResponse> getAllFilesByUser() {
        return ResponseEntity.ok(fileService.getAllFilesByUser());
    }
}
