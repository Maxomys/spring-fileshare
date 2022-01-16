package com.github.maxomys.springfileshare.api.controller.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileLinkDto {
    
    private Long id;
    private String url;
    private Integer remainingUses;
    private LocalDateTime expirationTime;
    private Long storedFileId;

}
