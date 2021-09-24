package com.github.maxomys.springfileshare.repository;

import com.github.maxomys.springfileshare.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {

    Optional<StoredFile> findStoredFileByFileName(String fileName);

    List<StoredFile> findStoredFilesByAppUser_Username(String username);

}
