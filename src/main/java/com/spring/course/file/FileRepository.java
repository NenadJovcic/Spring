package com.spring.course.file;

import com.spring.course.user.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing FileEntity entities using Spring Data JPA.
 */
@Repository
@CacheConfig(cacheNames = "files")
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    /**
     * Retrieves a list of files based on the ID of the folder they belong to.
     *
     * @param folderId The ID of the folder for which to retrieve files.
     * @return A list of files associated with the specified folder ID.
     */
    List<FileEntity> findByFolderId(Long folderId);

    /**
     * Retrieves a list of files associated with a specific user.
     *
     * @param user The user for whom to retrieve files.
     * @return A list of files associated with the specified user.
     */
    List<FileEntity> findByUser(User user);
}
