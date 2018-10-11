package com.dhb.learning.polls.auth.server.controller;

import com.dhb.learning.polls.auth.server.exception.AppException;
import com.dhb.learning.polls.auth.server.payload.ApiResponse;
import com.dhb.learning.polls.auth.server.payload.JwtAuthenticationResponse;
import com.dhb.learning.polls.auth.server.repository.RoleRepository;
import com.dhb.learning.polls.auth.server.repository.UserRepository;
import com.dhb.learning.polls.auth.server.model.Role;
import com.dhb.learning.polls.auth.server.model.RoleName;
import com.dhb.learning.polls.auth.server.model.User;
import com.dhb.learning.polls.auth.server.payload.LoginRequest;
import com.dhb.learning.polls.auth.server.payload.SignUpRequest;
import com.dhb.learning.polls.auth.server.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticationUser(
            @Valid
            @RequestBody
                    LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        // check if username is used before
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(
                    new ApiResponse(false, "Username is already token !"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // check if email is used before
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(
                    new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // creating user account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // set Roles

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role Not Set."));

        user.setRoles(Collections.singleton(userRole));

        User userResult = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(userResult.getUsername()).toUri();

        return ResponseEntity.created(location).body(
                new ApiResponse(true, "User registered successfully"));

    }

    @GetMapping("/find")
    public User findUser(@RequestParam Long id){
        return userRepository.findById(id).get();
    }
}
