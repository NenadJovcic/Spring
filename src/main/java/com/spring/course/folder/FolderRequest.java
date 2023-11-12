package com.spring.course.folder;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FolderRequest {
    private String name;
}
