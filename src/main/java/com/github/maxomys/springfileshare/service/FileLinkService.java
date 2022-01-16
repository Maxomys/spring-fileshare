package com.github.maxomys.springfileshare.service;

import java.time.LocalDateTime;

public interface FileLinkService {

    void addLinkToFile(Long fileId, Integer uses, String expirationDateTime);

    void removeLinkFromFileById(Long fileId, Long linkId);

}
