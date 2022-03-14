package com.github.maxomys.springfileshare.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.maxomys.springfileshare.api.dto.FileLinkDto;
import com.github.maxomys.springfileshare.api.mapper.FileLinkMapper;
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
    private final FileLinkMapper fileLinkMapper;

    @Override
    public List<FileLinkDto> getFileLinksByFileId(Long fileId) {
        return fileLinkRepository.findFileLinksByStoredFile_Id(fileId).stream()
                .map(fileLinkMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FileLink getFileLinkByUuid(String uuid) {
        return fileLinkRepository.findFileLinkByUrl(UUID.fromString(uuid)).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void addLinkToFile(FileLinkDto dto) {
        StoredFile storedFile = storedFileRepository.findById(dto.getStoredFileId()).orElseThrow(
            () -> new ResourceNotFoundException("StoredFile not found for id: " + dto.getStoredFileId()));

        FileLink fileLink = new FileLink();
        fileLink.setRemainingUses(dto.getRemainingUses());
        fileLink.setExpirationTime(dto.getExpirationTime());
        fileLink.setStoredFile(storedFile);
        fileLink.setUrl(UUID.randomUUID());

        fileLinkRepository.save(fileLink);
    }

    @Override
    public void removeLinkFromFileById(Long fileId, Long linkId) {
        StoredFile storedFile = storedFileRepository.findById(fileId).orElseThrow(
            () -> new ResourceNotFoundException("StoredFile not found for id: " + fileId));

        fileLinkRepository.deleteById(storedFile.getLinks().stream()
            .filter((link) -> link.getId().equals(linkId)).findFirst().orElseThrow(ResourceNotFoundException::new).getId());
    }
    
}
