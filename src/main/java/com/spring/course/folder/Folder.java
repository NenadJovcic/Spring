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
@ToString(exclude = "files")
@Entity
@Table(name = "folder")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    // Creation date defaults to the current date and time
    private LocalDateTime createdDate = LocalDateTime.now();

    // One-to-many relationship with FileEntity, mapped by the "folder" property
    // Cascade type is set to ALL for automatic persistence operations
    // Orphan removal is enabled, meaning if a file is removed from the list, it will be deleted
    // FetchType is set to LAZY for lazy loading
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FileEntity> files;

    // Many-to-one relationship with User, mapped by the "user_id" column
    // The relationship is not serialized to JSON to avoid infinite recursion (JsonIgnore)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
