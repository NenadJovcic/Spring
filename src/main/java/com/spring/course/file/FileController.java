package com.spring.course.file;

import com.spring.course.exception.ResourceNotFoundException;
import com.spring.course.exception.UnauthorizedAccessException;
import com.spring.course.exception.UserNotFoundException;
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

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folderId") Long folderId) throws IOException {
        fileService.uploadFile(file, folderId);
        return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFileById(@PathVariable Long id) {

        FileResponse downloadFileResponse = fileService.downloadFileById(id);
        ByteArrayResource resource = new ByteArrayResource(downloadFileResponse.getFileContent());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", downloadFileResponse.getFileName());

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FileResponse> removeFileById(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.removeFileById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> getFileById(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<FileResponse> getAllFilesByUser() {
        return ResponseEntity.ok(fileService.getAllFilesByUser());
    }
}
