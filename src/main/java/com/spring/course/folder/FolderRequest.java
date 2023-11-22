package com.spring.course.folder;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FolderRequest {
    @NotNull(message = "Name is required and cannot be null")
    private String name;
}
