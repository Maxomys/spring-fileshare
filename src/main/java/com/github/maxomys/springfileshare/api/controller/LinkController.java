package com.github.maxomys.springfileshare.api.controller;

import com.github.maxomys.springfileshare.api.dto.FileLinkDto;
import com.github.maxomys.springfileshare.model.FileLink;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.service.FileLinkService;
import com.github.maxomys.springfileshare.service.StoredFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LinkController {

    private final FileLinkService fileLinkService;
    private final StoredFileService storedFileService;

    @PostMapping("/link")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewLinkToFile(@RequestBody FileLinkDto fileLinkDto) {
        fileLinkService.addLinkToFile(fileLinkDto);
    }

    @GetMapping("/link/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public Resource downloadFileWithLink(@PathVariable String uuid, HttpServletResponse response) {
        FileLink fileLink = fileLinkService.getFileLinkByUuid(uuid);
        StoredFile storedFile = fileLink.getStoredFile();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + storedFile.getOriginalFileName());
        response.setHeader("filename", storedFile.getOriginalFileName());

        return storedFileService.loadFileByLink(fileLink);
    }

}
