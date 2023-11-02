package com.spring.course.service;

import com.spring.course.entity.FileEntity;
import com.spring.course.entity.Folder;
import com.spring.course.exception.ResourceNotFoundException;
import com.spring.course.repository.FileRepository;
import com.spring.course.repository.FolderRepository;
import com.spring.course.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FolderRepository folderRepository;

    public void uploadFile(MultipartFile file, Long folderId) throws IOException {
        Folder folder = folderRepository.findById(folderId).orElse(null);
        if (folder == null) {
            throw new IllegalArgumentException("Invalid folder ID.");
        }

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileContent(file.getBytes());
        fileEntity.setFolder(folder);

        fileRepository.save(fileEntity);
    }

    public ResponseEntity<Resource> downloadFileById(Long id) {
        try {
            Optional<FileEntity> optionalFile = fileRepository.findById(id);

            if (optionalFile.isPresent()) {
                FileEntity fileEntity = optionalFile.get();
                ByteArrayResource resource = new ByteArrayResource(fileEntity.getFileContent());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", fileEntity.getFileName());

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ApiResponse removeFileById(Long id) {
        Optional<FileEntity> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            fileRepository.deleteById(id);
            return ApiResponse.builder()
                    .message("Successfully deleted file with ID:" + id)
                    .build();
        } else {
            throw new ResourceNotFoundException("File not found with ID: " + id);
        }
    }

    public ApiResponse getFileById(Long id) {
        Optional<FileEntity> optionalFile = fileRepository.findById(id);
        if (optionalFile.isPresent()) {
            return ApiResponse.builder()
                    .message("Successfully found file with ID: " + id)
                    .data(optionalFile.get())
                    .build();
        } else {
            throw new ResourceNotFoundException("File not found with ID: " + id);
        }
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }
}







