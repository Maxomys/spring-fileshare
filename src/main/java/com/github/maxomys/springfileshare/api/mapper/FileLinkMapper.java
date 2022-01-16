package com.github.maxomys.springfileshare.api.mapper;

import com.github.maxomys.springfileshare.api.controller.dto.FileLinkDto;
import com.github.maxomys.springfileshare.model.FileLink;

public class FileLinkMapper {
    
    public FileLinkDto toDto(FileLink fileLink) {
        return FileLinkDto.builder()
            .id(fileLink.getId())
            .url(fileLink.getUrl())
            .remainingUses(fileLink.getRemainingUses())
            .expirationTime(fileLink.getExpirationTime())
            .storedFileId(fileLink.getStoredFile().getId())
            .build();
    }

}
