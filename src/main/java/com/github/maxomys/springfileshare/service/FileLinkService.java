package com.github.maxomys.springfileshare.service;

import com.github.maxomys.springfileshare.api.dto.FileLinkDto;
import com.github.maxomys.springfileshare.model.FileLink;

import java.util.List;

public interface FileLinkService {

    List<FileLinkDto> getFileLinksByFileId(Long fileId);

    FileLink getFileLinkByUuid(String uuid);

    void addLinkToFile(FileLinkDto dto);

    void removeLinkFromFileById(Long fileId, Long linkId);

}
