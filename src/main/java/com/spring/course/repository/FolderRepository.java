package com.spring.course.repository;

import com.spring.course.entity.Folder;
import com.spring.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByUser(User user);
}
