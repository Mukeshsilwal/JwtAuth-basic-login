package com.security.service;

import com.security.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User userDto);
    List<User> getAll();
}
