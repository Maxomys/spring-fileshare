package com.github.maxomys.springfileshare.service;

import com.github.maxomys.springfileshare.api.controller.dto.UserDto;
import com.github.maxomys.springfileshare.model.AppUser;

public interface AppUserService {

    AppUser findById(Long id);

    AppUser findByUsername(String username);

    AppUser saveUser(AppUser user);

    AppUser saveUser(UserDto userDto);

}
