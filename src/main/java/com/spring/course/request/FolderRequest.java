package com.spring.course.request;

import com.spring.course.entity.Folder;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FolderRequest {
    private String name;
}
