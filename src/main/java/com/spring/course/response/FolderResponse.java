package com.spring.course.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.course.entity.FileEntity;
import com.spring.course.entity.Folder;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FolderResponse {
    private boolean errorOccurred;
    private String message;
    private Folder folder;
    private List<Folder> folderList;
}
