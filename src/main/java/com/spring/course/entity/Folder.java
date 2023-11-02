package com.spring.course.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileEntity> files;

}
