package com.github.maxomys.springfileshare.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileLinkDto {
    
    private Long id;
    private Long storedFileId;
    private String url;
    private Integer remainingUses;
    private LocalDateTime expirationTime;

}
