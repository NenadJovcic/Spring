package com.spring.course;

import com.spring.course.config.JwtService;
import com.spring.course.file.FileController;
import com.spring.course.file.FileRepository;
import com.spring.course.file.FileResponse;
import com.spring.course.file.FileService;
import com.spring.course.folder.FolderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
@AutoConfigureMockMvc
public class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;
    @MockBean
    private FileService fileService;

    @MockBean
    private FolderRepository folderRepository;
    @MockBean
    private FileRepository fileRepository;

    @Test
    @WithMockUser
    public void uploadFileTest() throws Exception {
        // Mock the file service to do nothing (assuming successful upload)
        doNothing().when(fileService).uploadFile(any(), any());

        // Create a mock file for testing
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());

        // Perform the file upload
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/api/files/upload")
                        .file(file)
                        .param("folderId", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer your_token_here"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @WithMockUser
    public void removeFileByIdTest() throws Exception {
        // Mock the file service to return a FileResponse (you can customize the response as needed)
        doReturn(FileResponse.builder().message("File removed successfully").build()).when(fileService).removeFileById(any());

        // Perform the file removal by ID
        mockMvc.perform(delete("/api/files/{id}", 1L)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .header("Authorization", "Bearer your_token_here"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
