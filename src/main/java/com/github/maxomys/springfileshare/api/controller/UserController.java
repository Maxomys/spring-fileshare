package com.github.maxomys.springfileshare.api.controller;

import com.github.maxomys.springfileshare.api.dto.UserDto;
import com.github.maxomys.springfileshare.model.AppUser;
import com.github.maxomys.springfileshare.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final AppUserService userService;

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public AppUser getUserById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void postNewUser(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
    }

}
