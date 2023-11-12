package com.spring.course.folder;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FolderRequest {
    @NotNull
    private String name;
}
