package com.security.service.serviceImpl;

import com.security.repository.UserRepository;
import com.security.service.UserService;
import com.security.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceImpl implements UserService {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

    public ServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {

     user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public List<User> getAll() {


        return this.userRepository.findAll();
    }


}
