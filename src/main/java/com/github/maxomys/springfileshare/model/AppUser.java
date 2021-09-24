package com.github.maxomys.springfileshare.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    private String password;

    @NotNull
    @Email
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<StoredFile> storedFiles = new ArrayList<>();

}
