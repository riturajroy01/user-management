package com.users.management.controller;

import com.users.management.model.entity.Users;
import com.users.management.model.model.*;
import com.users.management.service.UserService;
import com.users.management.service.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private AuthenticationManager authenticationManager;
    Map<Long, Users> loginUsers = new HashMap<>();


    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(createUserRequestDto));
    }

    @Transactional
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(userId, userUpdateDto));
    }

    @Transactional
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(userId));
    }

    @Transactional
    @DeleteMapping("/{userId}")
    public ResponseEntity<DeleteResponseDto> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.deleteUser(userId));
    }


    @GetMapping("/login")
    public ResponseEntity<String> getLogin(@RequestBody LoginRequest loginRequest) {
        userService.getUserByEmailAndPassword(loginRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userPrincipal.getUser();
        loginUsers.put(user.getId(), user);
        return new ResponseEntity<>("User#" + user.getEmail() + " login successful", HttpStatus.OK);
    }

    @GetMapping("/logout/{userId}")
    public ResponseEntity<String> getLogOut(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.getUserById(userId);
        log.info("getLogOut userId: {} ", userId);
        if (!loginUsers.isEmpty() && loginUsers.get(userId) != null) {
            loginUsers.remove(userId);
            return new ResponseEntity<>("User# " + userResponseDto.getEmail() + " logout successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User# " + userResponseDto.getEmail() + " is not logged in. Please login to use this functionality...", HttpStatus.OK);
    }

}
