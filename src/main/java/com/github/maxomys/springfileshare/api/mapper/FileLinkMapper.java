package com.github.maxomys.springfileshare.api.mapper;

import com.github.maxomys.springfileshare.api.dto.FileLinkDto;
import com.github.maxomys.springfileshare.model.FileLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FileLinkMapper {

    @Mapping(source = "storedFile.id", target = "storedFileId")
    FileLinkDto toDto(FileLink fileLink);

    default String urlToString(UUID url) {
        return url.toString();
    }

}
