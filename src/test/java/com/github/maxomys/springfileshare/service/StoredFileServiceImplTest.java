package com.github.maxomys.springfileshare.service;

import com.github.maxomys.springfileshare.exception.ResourceNotFoundException;
import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.model.FileLink;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.repository.FileLinkRepository;
import com.github.maxomys.springfileshare.repository.StoredFileRepository;
import com.github.maxomys.springfileshare.utils.FileManager;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoredFileServiceImplTest {

    @Mock
    StoredFileRepository storedFileRepository;

    @Mock
    FileLinkRepository fileLinkRepository;

    @Mock
    FileManager fileManager;

    @InjectMocks
    StoredFileServiceImpl storedFileService;

    FileLink fileLink;
    StoredFile storedFile;

    @BeforeEach
    void setUp() {
        fileLink = FileLink.builder()
                .id(1L)
                .url(UUID.randomUUID())
                .remainingUses(1)
                .build();
        storedFile = StoredFile.builder()
                .id(1L)
                .fileName("fileName")
                .originalFileName("fileName")
                .size(1L)
                .links(Lists.newArrayList(fileLink))
                .build();
        fileLink.setStoredFile(storedFile);
    }

    @Test
    void findById() {
        when(storedFileRepository.findById(anyLong())).thenReturn(Optional.of(storedFile));

        StoredFile retrievedFile = storedFileService.findById(1L);

        assertEquals(1L, retrievedFile.getId());
    }

    @Test
    void findByIdNotFound() {
        when(storedFileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> storedFileService.findById(1L));
    }

    @Test
    void findByFilename() {
        when(storedFileRepository.findStoredFileByFileName(anyString())).thenReturn(Optional.of(storedFile));

        StoredFile retrievedFile = storedFileService.findByFilename("fileName");

        assertNotNull(retrievedFile);
    }

    @Test
    void findByFilenameNotFound() {
        when(storedFileRepository.findStoredFileByFileName(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> storedFileService.findByFilename("fileName"));
    }

    @Test
    void saveFile() {
        ArgumentCaptor<StoredFile> fileArgumentCaptor = ArgumentCaptor.forClass(StoredFile.class);

        storedFileService.saveFile(storedFile);

        verify(storedFileRepository).save(fileArgumentCaptor.capture());
        assertNotNull(fileArgumentCaptor.getValue().getCreatedAt());
    }

    @Test
    void getAllFilesByUsername() {
        storedFileService.getAllFilesByUsername("username");
        verify(storedFileRepository).findStoredFilesByAppUser_Username(anyString());
    }

    @Test
    void uploadFile() {
        ArgumentCaptor<StoredFile> storedFileCaptor = ArgumentCaptor.forClass(StoredFile.class);

        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                "originalFileName",
                MediaType.TEXT_PLAIN_VALUE,
                "a file".getBytes(StandardCharsets.UTF_8));

        AppUser user = new AppUser();
        user.setId(1L);

        storedFileService.uploadFile(multipartFile, user);

        verify(fileManager).saveFile(eq(multipartFile), any(StoredFile.class));
        verify(storedFileRepository).save(storedFileCaptor.capture());

        StoredFile savedFile = storedFileCaptor.getValue();

        assertEquals(user, savedFile.getAppUser());
        assertEquals(multipartFile.getOriginalFilename(), savedFile.getOriginalFileName());
        assertNotNull(savedFile.getFileName());
        assertNotNull(savedFile.getCreatedAt());
    }

    @Test
    void loadFileById() {
        when(storedFileRepository.findById(anyLong())).thenReturn(Optional.of(storedFile));

        storedFileService.loadFileById(1L);

        verify(fileManager).getFileResource(eq(storedFile));
    }

    @Test
    void loadFileByIdNotfound() {
        when(storedFileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> storedFileService.loadFileById(1L));
    }

    @Test
    void loadFileByLinkAndRemove() {
        storedFileService.loadFileByLink(fileLink);

        verify(fileLinkRepository).deleteById(eq(1L));
        assertEquals(0, fileLink.getRemainingUses());
        verify(fileManager).getFileResource(eq(storedFile));
    }

    @Test
    void deleteFileById() {

    }

}
