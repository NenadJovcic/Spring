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
import org.springframework.http.ResponseEntity;
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


    /**
     * Creates a folder based on the user's request.
     *
     * @param folderRequest A request to create a folder.
     * @return A response containing the created folder and a message.
     * @throws IllegalArgumentException Thrown if the request is invalid.
     */
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

    /**
     * Deletes a folder based on ID, provided the user has the authorization.
     *
     * @param id ID of the folder to be deleted.
     * @return A response containing a message that the folder has been deleted.
     * @throws UnauthorizedAccessException Thrown if the user does not have permission to delete the folder.
     * @throws ResourceNotFoundException   Thrown if the folder with the given ID is not found.
     */
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

    /**
     * Gets all folders belonging to the authenticated user.
     *
     * @return A response containing a list of folders.
     * @throws ResourceNotFoundException Thrown if no folders are found for the user.
     */
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

    /**
     * Gets a folder by ID, provided the user has the authorization.
     *
     * @param id ID of the folder to be retrieved.
     * @return A response containing the requested folder.
     * @throws ResourceNotFoundException   Thrown if the folder with the given ID is not found.
     * @throws UnauthorizedAccessException Thrown if the user does not have permission to access the folder.
     */
    public FolderResponse getFolderById(Long id) throws ResourceNotFoundException, UnauthorizedAccessException {
        User user = AuthenticationValidator.getAuthenticatedUser();
        Folder folder = folderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Folder not found with ID: " + id));


        if (!folder.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Unauthorized to access folder with ID: " + id);
        }
        return FolderResponse.builder()
                .folder(folder)
                .build();
    }
}
