package com.spring.course.service;

import com.spring.course.entity.Folder;
import com.spring.course.repository.FolderRepository;
import com.spring.course.request.FolderRequest;
import com.spring.course.response.FolderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;


    public FolderResponse createFolder(FolderRequest folderRequest) {
        Folder folder = Folder.builder()
                .name(folderRequest.getName())
                .createdDate(LocalDateTime.now())
                .build();
         folderRepository.save(folder);

        return FolderResponse.builder()
                .folder(folder)
                .message("Successfully created Folder: ")
                .build();
    }
}
