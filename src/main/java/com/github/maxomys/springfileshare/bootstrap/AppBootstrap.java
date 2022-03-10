package com.github.maxomys.springfileshare.bootstrap;

import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.model.FileLink;
import com.github.maxomys.springfileshare.model.Role;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.service.AppUserService;
import com.github.maxomys.springfileshare.service.StoredFileService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Component
@Profile("h2dev")
@RequiredArgsConstructor
public class AppBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final AppUserService appUserService;
    private final StoredFileService storedFileService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //User1
        AppUser user1 = new AppUser();

        user1.setUsername("Joe");
        user1.setPassword("letmein");
        user1.setUserRole(Role.ROLE_USER);
        user1.setEmailAddress("joemama@gmail.com");

        //File1
        StoredFile file1 = new StoredFile();
        
        file1.setOriginalFileName("foo.jpg");
        file1.setAppUser(user1);
        file1.setCreatedAt(LocalDateTime.now());
        file1.setSize(1048576L * 30);

        FileLink link1 = new FileLink();
        link1.setRemainingUses(10);
        link1.setUrl(UUID.randomUUID());
        link1.setStoredFile(file1);

        file1.getLinks().add(link1);

        //File2
        StoredFile file2 = new StoredFile();
        
        file2.setOriginalFileName("bar.pdf");
        file2.setAppUser(user1);
        file2.setCreatedAt(LocalDateTime.now());
        file2.setSize(1048576L * 30);

        FileLink link2 = new FileLink();
        link2.setRemainingUses(10);
        link2.setUrl(UUID.randomUUID());
        link2.setStoredFile(file2);

        file2.getLinks().add(link2);

        user1.getStoredFiles().addAll(List.of(file1, file2));
        appUserService.saveUser(user1);
    }

}
