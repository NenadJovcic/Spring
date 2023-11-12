package com.spring.course.file;

import com.spring.course.file.FileEntity;
import com.spring.course.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByFolderId(Long folderId);

    List<FileEntity> findByUser(User user);
}
