package com.github.maxomys.springfileshare.utils;

import com.github.maxomys.springfileshare.model.StoredFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileManagerImpl implements FileManager {

    @Value("${resources.directory}")
    String RESOURCES_ROOT;

    @Override
    public void saveFile(MultipartFile file, StoredFile newFile) {
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
    }

    @Override
    public Resource getFileResource(StoredFile foundFile) {
        Resource resource = new FileSystemResource(Paths.get(RESOURCES_ROOT, foundFile.getFileName()));
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("Could not load the file");
        }
    }

}
