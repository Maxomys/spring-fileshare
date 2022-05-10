package com.github.maxomys.springfileshare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID url;

    @NotNull
    private Integer remainingUses;

    private LocalDateTime expirationTime;

    @ManyToOne
    @JsonBackReference
    private StoredFile storedFile;

}
