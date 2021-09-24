package com.github.maxomys.springfileshare.api.controller;

import java.util.List;

import com.github.maxomys.springfileshare.exception.WrongUserException;
import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.service.AppUserService;
import com.github.maxomys.springfileshare.service.StoredFileService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    
    private final StoredFileService storedFileService;
    private final AppUserService appUserService;

    @GetMapping(value = "/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    public StoredFile getFileById(@PathVariable Long fileId) {
        return storedFileService.findById(fileId);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<StoredFile> getAllFilesByUsername(@AuthenticationPrincipal String username) {
        return storedFileService.getAllFilesByUsername(username);
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(@RequestParam("file") MultipartFile multipartFile, @AuthenticationPrincipal String username) {
        AppUser appUser = appUserService.findByUsername(username);

        storedFileService.uploadFile(multipartFile, appUser);
    }

    @GetMapping("/{fileId}/download")
    public Resource downloadFile(@PathVariable Long fileId, HttpServletResponse response, @AuthenticationPrincipal String username) {
        StoredFile storedFile = storedFileService.findById(fileId);

        if (!storedFile.getAppUser().getUsername().equals(username)) {
            throw new WrongUserException("User " + username + " is not the owner of the file");
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + storedFile.getOriginalFileName());
        response.setHeader("filename", storedFile.getOriginalFileName());

        return storedFileService.loadFileById(fileId);
    }

}
