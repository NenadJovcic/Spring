package com.spring.course.user;

import com.spring.course.file.FileEntity;
import com.spring.course.folder.Folder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entity class representing a user in the system.
 */
@Data
@Builder
@ToString(exclude = "folders")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;

    /**
     * The role assigned to the user (e.g., ROLE_USER, ROLE_ADMIN).
     */
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /**
     * List of folders associated with the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> folders;
    /**
     * List of files associated with the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
