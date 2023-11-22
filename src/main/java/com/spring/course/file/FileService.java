package com.spring.course.file;

import com.spring.course.context.AuthenticationValidator;
import com.spring.course.exception.MissingFileException;
import com.spring.course.folder.Folder;
import com.spring.course.user.User;
import com.spring.course.exception.ResourceNotFoundException;
import com.spring.course.exception.UnauthorizedAccessException;
import com.spring.course.exception.UserNotFoundException;
import com.spring.course.folder.FolderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Uploads a file to the specified folder.
     *
     * @param file     The file to upload.
     * @param folderId The ID of the folder where the file will be uploaded.
     * @throws IllegalArgumentException    If the folder ID is invalid.
     * @throws UnauthorizedAccessException If the user is unauthorized to upload to the folder.
     * @throws IOException                 If an IO error occurs during file processing.
     * @throws MissingFileException        If the file is null, empty, or has no content.
     * @throws UserNotFoundException       If the authenticated user is not found.
     */
    @Transactional
    public void uploadFile(MultipartFile file, Long folderId) throws IllegalArgumentException,
            UnauthorizedAccessException,
            IOException,
            MissingFileException,
            UserNotFoundException {
        User user = AuthenticationValidator.getAuthenticatedUser();
        if (folderId == null) {
            throw new IllegalArgumentException("Folder ID cannot be null, please include in request");
        }
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new ResourceNotFoundException("File with ID: " + folderId + " not found"));

        if (file == null || file.isEmpty() || file.getSize() == 0) {
            throw new MissingFileException("File is null or empty. Please provide a valid file.");
        }

        if (!folder.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Unauthorized to Upload to folder with ID: " + folderId);
        }

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileContent(file.getBytes());
        fileEntity.setFolder(folder);
        fileEntity.setUser(user);

        fileRepository.save(fileEntity);
    }

    /**
     * Downloads a file by its ID.
     *
     * @param id The ID of the file to download.
     * @return FileResponse containing the file information.
     * @throws ResourceNotFoundException   If the file with the specified ID is not found.
     * @throws UnauthorizedAccessException If the user is unauthorized to download the file.
     * @throws UserNotFoundException       If the authenticated user is not found.
     */
    public FileResponse downloadFileById(Long id) throws ResourceNotFoundException, UnauthorizedAccessException, UserNotFoundException {
        User user = AuthenticationValidator.getAuthenticatedUser();
        FileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File with ID: " + id + " not found"));

        if (!file.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Unauthorized to download file with ID: " + id);
        }

        return FileResponse.builder()
                .fileName(file.getFileName())
                .fileContent(file.getFileContent())
                .build();
    }


    /**
     * Removes a file by its ID.
     *
     * @param id The ID of the file to remove.
     * @return FileResponse indicating the deletion success.
     * @throws ResourceNotFoundException   If the file with the specified ID is not found.
     * @throws UnauthorizedAccessException If the user is unauthorized to delete the file.
     * @throws UserNotFoundException       If the authenticated user is not found.
     */
    public FileResponse removeFileById(Long id) throws UnauthorizedAccessException, ResourceNotFoundException, UserNotFoundException {
        User user = AuthenticationValidator.getAuthenticatedUser();

        FileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with ID: " + id));

        if (!file.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Unauthorized to delete file with ID: " + id);
        }

        fileRepository.deleteById(id);
        return FileResponse.builder().message("Successfully deleted file with ID: " + id).build();
    }

    /**
     * Retrieves a file by its ID.
     *
     * @param id The ID of the file to retrieve.
     * @return FileResponse containing the file information.
     * @throws UnauthorizedAccessException If the user is unauthorized to access the file.
     * @throws ResourceNotFoundException   If the file with the specified ID is not found.
     * @throws UserNotFoundException       If the authenticated user is not found.
     */
    public FileResponse getFileById(Long id) throws UnauthorizedAccessException, ResourceNotFoundException, UserNotFoundException {
        User user = AuthenticationValidator.getAuthenticatedUser();
        FileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with ID: " + id));

        // Check if the authenticated user is the owner of the file
        if (!file.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to access file with ID: " + id);
        }

        return FileResponse.builder().message("Successfully found file with ID: " + id).file(file).build();
    }

    /**
     * Retrieves all files associated with the authenticated user.
     *
     * @return FileResponse containing the list of files.
     * @throws UserNotFoundException     If the authenticated user is not found.
     * @throws ResourceNotFoundException If no files are found for the user.
     */
    public FileResponse getAllFilesByUser() throws UserNotFoundException, ResourceNotFoundException {
        User user = AuthenticationValidator.getAuthenticatedUser();

        List<FileEntity> files = Optional.ofNullable(fileRepository.findByUser(user))
                .orElseThrow(() -> new ResourceNotFoundException("No files found for the user"));

        return FileResponse.builder().files(files).build();
    }
}







