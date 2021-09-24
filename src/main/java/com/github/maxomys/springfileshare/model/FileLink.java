package com.github.maxomys.springfileshare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
public class FileLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    private String url;

    @NotNull
    private Integer remainingUses;

    private LocalDateTime expirationTime;

    @ManyToOne
    @JsonBackReference
    private StoredFile storedFile;

}
