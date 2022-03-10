package com.github.maxomys.springfileshare.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.github.maxomys.springfileshare.exception.ResourceNotFoundException;
import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.model.FileLink;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.repository.FileLinkRepository;
import com.github.maxomys.springfileshare.repository.StoredFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoredFileServiceImpl implements StoredFileService {

    @Value("${resources.directory}")
    String RESOURCES_ROOT;

    private final StoredFileRepository storedFileRepository;
    private final FileLinkRepository fileLinkRepository;

    @Override
    public StoredFile findById(Long id) {
        return storedFileRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("File not found for id: " + id));
    }

    @Override
    public StoredFile findByFilename(String fileName) {
        return storedFileRepository.findStoredFileByFileName(fileName).orElseThrow(() -> new ResourceNotFoundException("File not found for filename: " + fileName));
    }

    @Override
    public StoredFile saveFile(StoredFile file) {
        file.setCreatedAt(LocalDateTime.now());
        
        return storedFileRepository.save(file);
    }

    @Override
    public List<StoredFile> getAllFiles() {
        return storedFileRepository.findAll();
    }

    @Override
    public List<StoredFile> getAllFilesByUsername(String username) {
        return storedFileRepository.findStoredFilesByAppUser_Username(username);
    }

    @Override
    public void uploadFile(MultipartFile file, AppUser appUser) {

        StoredFile newFile = new StoredFile();
        newFile.setAppUser(appUser);
        newFile.setFileName(UUID.randomUUID().toString());
        newFile.setOriginalFileName(file.getOriginalFilename());
        newFile.setCreatedAt(LocalDateTime.now());

        Path savePath = Paths.get(RESOURCES_ROOT, newFile.getFileName());

        try {
            Files.copy(file.getInputStream(), savePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file " + e.getMessage());
        }

        File savedFile = savePath.toFile();
        if (savedFile.isFile()) {
            newFile.setSize(savedFile.length());
        }

        storedFileRepository.save(newFile);
    }

    @Override
    public Resource loadFileById(Long id) {
        StoredFile foundFile = storedFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find file for id: " + id));

        Resource resource = new FileSystemResource(Paths.get(RESOURCES_ROOT, foundFile.getFileName()));
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("Could not load the file");
        }
    }

    @Override
    public Resource loadFileByLink(FileLink fileLink) {
        StoredFile foundFile = fileLink.getStoredFile();

        fileLink.setRemainingUses(fileLink.getRemainingUses() - 1);
        if (fileLink.getRemainingUses() <= 0) {
            fileLinkRepository.deleteById(fileLink.getId());
        }

        Resource resource = new FileSystemResource(Paths.get(RESOURCES_ROOT, foundFile.getFileName()));
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("Could not load the file");
        }
    }

    @Override
    public void deleteFileById(Long id) {

    }

}
