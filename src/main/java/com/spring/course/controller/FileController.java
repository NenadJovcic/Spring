package com.spring.course.controller;

import com.spring.course.entity.FileEntity;
import com.spring.course.entity.Folder;
import com.spring.course.repository.FileRepository;
import com.spring.course.repository.FolderRepository;
import com.spring.course.response.ApiResponse;
import com.spring.course.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
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
            Folder folder = folderRepository.findById(folderId).orElse(null);
            if (folder == null) {
                return new ResponseEntity<>("Invalid folder name.", HttpStatus.BAD_REQUEST);
            }


            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileContent(file.getBytes());
            fileEntity.setFolder(folder);
            fileRepository.save(fileEntity);

            return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload the file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Optional<FileEntity> optionalFile = fileRepository.findById(id);

        if (optionalFile.isPresent()) {
            FileEntity fileEntity = optionalFile.get();
            byte[] fileContent = fileEntity.getFileContent();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", new String(fileEntity.getFileName().getBytes(), StandardCharsets.ISO_8859_1));
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> removeFileById(@PathVariable Long id) {
        Optional<FileEntity> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            fileRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Successfully deleted file with ID:" + id)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                    .message("File not found with ID: " + id)
                    .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileEntity> getFileById(@PathVariable Long id) {
        Optional<FileEntity> optionalFile = fileRepository.findById(id);

        return optionalFile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileEntity>> getAllFiles() {
        List<FileEntity> files = fileRepository.findAll();
        if (!files.isEmpty()) {
            return ResponseEntity.ok(files);
        }
        return ResponseEntity.notFound().build();
    }


}
