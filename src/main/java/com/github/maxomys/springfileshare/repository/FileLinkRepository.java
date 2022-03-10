package com.github.maxomys.springfileshare.repository;

import com.github.maxomys.springfileshare.model.FileLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileLinkRepository extends JpaRepository<FileLink, Long> {

    List<FileLink> findFileLinksByStoredFile_Id(Long storedFileId);

    Optional<FileLink> findFileLinkByUrl(UUID url);

    void deleteById(Long id);

}
