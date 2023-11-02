package com.spring.course.response;

import com.spring.course.entity.Folder;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FolderResponse {
    private boolean errorOccurred;
    private String message;
    private Folder folder;
}
