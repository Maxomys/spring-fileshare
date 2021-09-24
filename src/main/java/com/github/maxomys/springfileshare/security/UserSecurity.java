package com.github.maxomys.springfileshare.security;

import com.github.maxomys.springfileshare.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {

    private final AppUserService appUserService;

    public boolean hasUserId(Authentication authentication, Long userId) {
        return appUserService.findByUsername((String) authentication.getPrincipal()).getId().equals(userId);
    }

}
