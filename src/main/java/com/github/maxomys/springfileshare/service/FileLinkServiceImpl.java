package com.github.maxomys.springfileshare.service;

import java.time.LocalDateTime;

import com.github.maxomys.springfileshare.exception.ResourceNotFoundException;
import com.github.maxomys.springfileshare.model.FileLink;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.repository.FileLinkRepository;
import com.github.maxomys.springfileshare.repository.StoredFileRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileLinkServiceImpl implements FileLinkService {

    private final StoredFileRepository storedFileRepository;
    private final FileLinkRepository fileLinkRepository;

    @Override
    public void addLinkToFile(Long fileId, Integer uses, String expirationDateTime) {
        StoredFile storedFile = storedFileRepository.findById(fileId).orElseThrow(
            () -> new ResourceNotFoundException("StoredFile not found for id: " + fileId));
        
        FileLink fileLink = new FileLink();
        fileLink.setRemainingUses(uses);
        fileLink.setExpirationTime(LocalDateTime.parse(expirationDateTime));
        fileLink.setStoredFile(storedFile);

        fileLinkRepository.save(fileLink);
    }

    @Override
    public void removeLinkFromFileById(Long fileId, Long linkId) {
        StoredFile storedFile = storedFileRepository.findById(fileId).orElseThrow(
            () -> new ResourceNotFoundException("StoredFile not found for id: " + fileId));

        fileLinkRepository.deleteById(storedFile.getLinks().stream()
            .filter((link) -> link.getId() == linkId).findFirst().orElse(null));
    }
    
}
