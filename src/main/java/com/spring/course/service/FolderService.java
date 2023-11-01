package com.spring.course.service;

import com.spring.course.entity.Folder;
import com.spring.course.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;


    public Folder createFolder(Folder folder) {
        return folderRepository.save(folder);
    }
}
