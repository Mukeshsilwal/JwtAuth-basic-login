package com.security.controller;

import com.security.entity.User;
import com.security.security.JwtHelper;
import com.security.service.UserService;
import com.security.model.JwtRequest;
import com.security.model.JwtResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class LoginController {

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    public LoginController(UserDetailsService userDetailsService, UserService userService, AuthenticationManager authenticationManager, JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest user) {

        this.doAuthenticate(user.getEmail(), user.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);
        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {

            throw new RuntimeException("Username Password incorrect!!");
        }


    }

    @PostMapping("/create_user")
    public User createUser(@RequestBody User user) {
        return this.userService.createUser(user);

    }
}
