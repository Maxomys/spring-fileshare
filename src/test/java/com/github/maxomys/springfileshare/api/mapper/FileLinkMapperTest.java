package com.github.maxomys.springfileshare.api.mapper;

import com.github.maxomys.springfileshare.api.dto.FileLinkDto;
import com.github.maxomys.springfileshare.model.FileLink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {FileLinkMapperImpl.class})
class FileLinkMapperTest {

    @Autowired
    FileLinkMapper fileLinkMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void toDto() {
        FileLink link = FileLink.builder()
                .id(1L)
                .url(UUID.randomUUID())
                .remainingUses(1)
                .build();

        FileLinkDto dto = fileLinkMapper.toDto(link);

        assertEquals(1, dto.getId());
        assertNotNull(dto.getUrl());
        assertNull(dto.getExpirationTime());
    }

}
