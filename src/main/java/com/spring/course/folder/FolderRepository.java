package com.spring.course.folder;

import com.spring.course.folder.Folder;
import com.spring.course.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByUser(User user);
}
