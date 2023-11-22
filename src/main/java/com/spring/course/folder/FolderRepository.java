package com.spring.course.folder;

import com.spring.course.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Folder entities using Spring Data JPA.
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    /**
     * Retrieves a list of folders associated with a specific user.
     *
     * @param user The user for whom to retrieve folders.
     * @return A list of folders associated with the specified user.
     */
    List<Folder> findByUser(User user);
}
