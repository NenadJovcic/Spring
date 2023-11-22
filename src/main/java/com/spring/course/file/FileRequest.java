package com.spring.course.file;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FileRequest {
    @NotNull(message = "File must be included an cannot be null")
    private MultipartFile file;

    @NotNull(message = "FolderId must be included and cannot be null")
    @Min(value = 1, message = "FolderId must be a positive integer")
    private Long folderId;
}
