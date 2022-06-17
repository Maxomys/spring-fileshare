package com.github.maxomys.springfileshare.api.controller;

import com.github.maxomys.springfileshare.configuration.Config;
import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.model.StoredFile;
import com.github.maxomys.springfileshare.service.AppUserService;
import com.github.maxomys.springfileshare.service.StoredFileService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
@Import(Config.class)
class FileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StoredFileService storedFileService;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    AppUserService appUserService;

    StoredFile storedFile;
    AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("user");
        storedFile = StoredFile.builder()
                .id(1L)
                .fileName("fileName")
                .originalFileName("fileName")
                .size(1L)
                .appUser(appUser)
                .build();
    }

    @Test
    void uploadFile() {
    }

    @Test
    @WithMockUser
    void downloadFile() throws Exception {
        UserDetails userDetails = new User("user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(storedFileService.findById(anyLong())).thenReturn(storedFile);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        mockMvc.perform(get("/api/file/1/download").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(header().string("filename", storedFile.getOriginalFileName()));

        verify(storedFileService).loadFileById(anyLong());
    }

}
