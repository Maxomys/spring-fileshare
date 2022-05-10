package com.github.maxomys.springfileshare.service;

import com.github.maxomys.springfileshare.exception.ResourceNotFoundException;
import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.model.FileLink;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.repository.FileLinkRepository;
import com.github.maxomys.springfileshare.repository.StoredFileRepository;
import com.github.maxomys.springfileshare.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoredFileServiceImpl implements StoredFileService {

    private final StoredFileRepository storedFileRepository;
    private final FileLinkRepository fileLinkRepository;
    private final FileManager fileManager;

    @Override
    public StoredFile findById(Long id) {
        return storedFileRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("File not found for id: " + id));
    }

    @Override
    public StoredFile findByFilename(String fileName) {
        return storedFileRepository.findStoredFileByFileName(fileName)
                .orElseThrow(() -> new ResourceNotFoundException("File not found for filename: " + fileName));
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

        fileManager.saveFile(file, newFile);

        storedFileRepository.save(newFile);
    }

    @Override
    public Resource loadFileById(Long id) {
        StoredFile foundFile = storedFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find file for id: " + id));

        return fileManager.getFileResource(foundFile);
    }

    @Override
    public Resource loadFileByLink(FileLink fileLink) {
        StoredFile foundFile = fileLink.getStoredFile();

        fileLink.setRemainingUses(fileLink.getRemainingUses() - 1);
        if (fileLink.getRemainingUses() <= 0) {
            fileLinkRepository.deleteById(fileLink.getId());
        }

        return fileManager.getFileResource(foundFile);
    }

    @Override
    public void deleteFileById(Long id) {

    }

}
