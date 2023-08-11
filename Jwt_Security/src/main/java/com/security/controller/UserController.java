package com.security.controller;

import com.security.entity.User;
import com.security.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user){

        return this.userService.createUser(user);
    }
    @GetMapping("/user")
    public List<User> getAll(){

        return this.userService.getAll();
    }
}
