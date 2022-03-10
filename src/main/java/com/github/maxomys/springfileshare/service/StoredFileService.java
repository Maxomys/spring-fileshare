package com.github.maxomys.springfileshare.service;

import java.util.List;

import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.model.FileLink;
import com.github.maxomys.springfileshare.model.StoredFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StoredFileService {

    StoredFile findById(Long id);

    StoredFile findByFilename(String fileName);

    StoredFile saveFile(StoredFile file);

    List<StoredFile> getAllFiles();

    List<StoredFile> getAllFilesByUsername(String username);

    void uploadFile(MultipartFile file, AppUser appUser);

    Resource loadFileById(Long id);

    Resource loadFileByLink(FileLink fileLink);

    void deleteFileById(Long id);

}
