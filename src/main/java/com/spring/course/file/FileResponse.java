package com.spring.course.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.course.file.FileEntity;
import lombok.*;
import org.springframework.http.HttpHeaders;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileResponse {
    private String fileName;
    private String message;
    private HttpHeaders headers;
    private byte[] fileContent;
    private List<FileEntity> files;
    private FileEntity file;
}
