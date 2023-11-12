package com.spring.course.folder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.course.user.User;
import com.spring.course.file.FileEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private LocalDateTime createdDate = LocalDateTime.now();


    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FileEntity> files;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
