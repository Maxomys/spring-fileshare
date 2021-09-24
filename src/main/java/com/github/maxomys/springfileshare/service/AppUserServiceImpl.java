package com.github.maxomys.springfileshare.service;

import com.github.maxomys.springfileshare.api.controller.dto.UserDto;
import com.github.maxomys.springfileshare.exception.ResourceNotFoundException;
import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.model.Role;
import com.github.maxomys.springfileshare.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("App user not found for Id: " + id));
    }

    @Override
    public AppUser findByUsername(String username) {
        return appUserRepository.findAppUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("App user not found for Username: " + username));
    }

    @Override
    public AppUser saveUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public AppUser saveUser(UserDto userDto) {
        AppUser newUser = new AppUser();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setEmailAddress(userDto.getEmail());
        newUser.setUserRole(Role.ROLE_USER);

        return appUserRepository.save(newUser);
    }

}
