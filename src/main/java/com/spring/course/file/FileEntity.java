package com.spring.course.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.course.folder.Folder;
import com.spring.course.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing a file in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "folder")
@Builder
@Entity
@Table(name = "file_entity")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;

    /**
     * Many-to-one relationship with Folder. The folder that contains the file.
     * The @JoinColumn annotation specifies the foreign key column in the file_entity table.
     * The @JsonIgnore annotation prevents JSON serialization to avoid circular references.
     */
    @ManyToOne
    @JoinColumn(name = "folder_id")
    @JsonIgnore
    private Folder folder;

    /**
     * Binary content of the file stored as a byte array.
     * The @Lob annotation is used for large binary objects (BLOBs).
     * The FetchType. LAZY ensures that file content is loaded lazily when needed.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileContent;

    /**
     * Many-to-one relationship with User. The user who owns the file.
     * The @JoinColumn annotation specifies the foreign key column in the file_entity table.
     * The @JsonIgnore annotation prevents JSON serialization to avoid circular references.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
