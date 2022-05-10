package com.github.maxomys.springfileshare.service;

import com.github.maxomys.springfileshare.api.dto.FileLinkDto;
import com.github.maxomys.springfileshare.api.mapper.FileLinkMapper;
import com.github.maxomys.springfileshare.api.mapper.FileLinkMapperImpl;
import com.github.maxomys.springfileshare.exception.ResourceNotFoundException;
import com.github.maxomys.springfileshare.model.FileLink;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.repository.FileLinkRepository;
import com.github.maxomys.springfileshare.repository.StoredFileRepository;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {FileLinkMapperImpl.class})
class FileLinkServiceImplTest {

    @Autowired
    FileLinkMapper fileLinkMapper;

    @Mock
    StoredFileRepository storedFileRepository;

    @Mock
    FileLinkRepository fileLinkRepository;

    FileLinkServiceImpl fileLinkService;

    FileLink fileLink;
    StoredFile storedFile;
    UUID urlUUID;

    @BeforeEach
    void setUp() {
        fileLinkService = new FileLinkServiceImpl(storedFileRepository, fileLinkRepository, fileLinkMapper);

        urlUUID = UUID.randomUUID();
        fileLink = FileLink.builder()
                .id(1L)
                .url(urlUUID)
                .remainingUses(1)
                .build();

        storedFile = StoredFile.builder()
                .id(1L)
                .originalFileName("fileName")
                .size(1L)
                .links(Lists.newArrayList(fileLink))
                .build();
    }

    @Test
    void getFileLinksByFileId() {
        when(fileLinkRepository.findFileLinksByStoredFile_Id(anyLong())).thenReturn(List.of(fileLink));

        List<FileLinkDto> fileLinkDtos = fileLinkService.getFileLinksByFileId(1L);

        assertEquals(1, fileLinkDtos.size());
        assertEquals(1, fileLinkDtos.get(0).getId());
    }

    @Test
    void getFileLinkByUuid() {
        when(fileLinkRepository.findFileLinkByUrl(any(UUID.class))).thenReturn(Optional.of(fileLink));

        FileLink returnedLink = fileLinkService.getFileLinkByUuid(urlUUID.toString());

        assertEquals(urlUUID, returnedLink.getUrl());
    }

    @Test
    void getFileLinkByUuidNotFound() {
        when(fileLinkRepository.findFileLinkByUrl(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> fileLinkService.getFileLinkByUuid(UUID.randomUUID().toString()));
    }

    @Test
    void addLinkToFile() {
        ArgumentCaptor<FileLink> fileLinkCaptor = ArgumentCaptor.forClass(FileLink.class);
        FileLinkDto fileLinkDto = FileLinkDto.builder()
                .storedFileId(1L)
                .remainingUses(1)
                .expirationTime(LocalDateTime.now())
                .build();

        when(storedFileRepository.findById(anyLong())).thenReturn(Optional.of(storedFile));

        fileLinkService.addLinkToFile(fileLinkDto);

        verify(fileLinkRepository).save(fileLinkCaptor.capture());
        FileLink capturedFileLink = fileLinkCaptor.getValue();

        assertEquals(storedFile, capturedFileLink.getStoredFile());
        assertEquals(1, capturedFileLink.getRemainingUses());
        assertNotNull(capturedFileLink.getUrl());
    }

    @Test
    void removeLinkFromFileById() {
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(storedFileRepository.findById(anyLong())).thenReturn(Optional.of(storedFile));

        fileLinkService.removeLinkFromFileById(1L, 1L);

        verify(fileLinkRepository).deleteById(idCaptor.capture());
        assertEquals(1L, idCaptor.getValue());
    }

}
