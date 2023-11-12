package com.spring.course.folder;

import com.spring.course.context.AuthenticationValidator;
import com.spring.course.file.FileService;
import com.spring.course.folder.Folder;
import com.spring.course.user.User;
import com.spring.course.exception.ResourceNotFoundException;
import com.spring.course.exception.UnauthorizedAccessException;
import com.spring.course.folder.FolderRepository;
import com.spring.course.folder.FolderRequest;
import com.spring.course.folder.FolderResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FileService fileService;


    public FolderResponse createFolder(FolderRequest folderRequest) throws IllegalArgumentException {
        User user = AuthenticationValidator.getAuthenticatedUser();

        if (folderRequest == null || folderRequest.getName() == null || folderRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Invalid folder request for creating a folder. Please include name");
        }

        Folder folder = Folder.builder()
                .name(folderRequest.getName())
                .createdDate(LocalDateTime.now())
                .user(user)
                .build();
        folderRepository.save(folder);

        return FolderResponse.builder()
                .folder(folder)
                .message("Successfully created Folder: ")
                .build();
    }

    public FolderResponse deleteFolderById(Long id) {
        User user = AuthenticationValidator.getAuthenticatedUser();
        Optional<Folder> folderOptional = folderRepository.findById(id);

        if (folderOptional.isPresent()) {
            Folder folder = folderOptional.get();
            if (folder.getUser().getId().equals(user.getId())) {
                folderRepository.deleteById(id);
                return FolderResponse.builder()
                        .message("Successfully deleted folder with ID: " + id)
                        .build();
            } else {
                throw new UnauthorizedAccessException("You do not have permission to delete this folder");
            }
        } else {
            throw new ResourceNotFoundException("Folder not found with id: " + id);
        }
    }


    public FolderResponse getAllFoldersByUser() {
        User user = AuthenticationValidator.getAuthenticatedUser();
        List<Folder> folders = folderRepository.findByUser(user);

        if (folders.isEmpty()) {
            throw new ResourceNotFoundException("No foulders found for the user");
        }

        return FolderResponse.builder()
                .folderList(folders)
                .build();
    }
}
