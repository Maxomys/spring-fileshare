package com.github.maxomys.springfileshare.api.mapper;

import com.github.maxomys.springfileshare.api.dto.FileLinkDto;
import com.github.maxomys.springfileshare.model.FileLink;
import org.springframework.stereotype.Component;

@Component
public class FileLinkMapper {
    
    public FileLinkDto toDto(FileLink fileLink) {
        return FileLinkDto.builder()
            .id(fileLink.getId())
            .url(fileLink.getUrl().toString())
            .remainingUses(fileLink.getRemainingUses())
            .expirationTime(fileLink.getExpirationTime())
            .storedFileId(fileLink.getStoredFile().getId())
            .build();
    }


}
