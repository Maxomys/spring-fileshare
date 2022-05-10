package com.github.maxomys.springfileshare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @NotBlank
    private String originalFileName;

    private Long size;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonBackReference
    private AppUser appUser;

    @OneToMany(mappedBy = "storedFile", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FileLink> links = new ArrayList<>();

}
