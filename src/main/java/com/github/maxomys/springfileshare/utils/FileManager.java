package com.github.maxomys.springfileshare.utils;

import com.github.maxomys.springfileshare.model.StoredFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileManager {

    void saveFile(MultipartFile file, StoredFile newFile);

    Resource getFileResource(StoredFile foundFile);

}
