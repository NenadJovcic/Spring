package com.spring.course.folder;

import com.fasterxml.jackson.annotation.JsonInclude;
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
