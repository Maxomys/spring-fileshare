package com.github.maxomys.springfileshare.utils;

import com.github.maxomys.springfileshare.exception.ServerErrorException;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Profile("gcloud")
@Primary
@Component
@RequiredArgsConstructor
public class FileManagerGcloudImpl implements FileManager {

    @Value("${BUCKET_NAME}")
    private String BUCKET_NAME;

    private final Storage storage;

    @Override
    public void saveFile(MultipartFile file, StoredFile newFile) {
        Bucket bucket = storage.get(BUCKET_NAME);

        try {
            bucket.create(newFile.getFileName(), file.getInputStream());
        } catch (IOException e) {
            throw new ServerErrorException("File not saved!", e);
        }
    }

    @Override
    public Resource getFileResource(StoredFile foundFile) {
        Bucket bucket = storage.get(BUCKET_NAME);
        Blob blob = bucket.get(foundFile.getFileName());
        return new ByteArrayResource(blob.getContent());
    }

}
