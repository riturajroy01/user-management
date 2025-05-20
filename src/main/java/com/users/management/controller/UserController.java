package com.users.management.controller;

import com.users.management.model.entity.Users;
import com.users.management.model.model.*;
import com.users.management.service.UserService;
import com.users.management.service.security.MyUserDetailsService;
import com.users.management.service.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;


    private AuthenticationManager authenticationManager;

    private ApplicationContext context;

    Map<Long, Users> loginUsers = new HashMap<>();


    public UserController(UserService userService, AuthenticationManager authenticationManager, ApplicationContext context) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.context = context;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(createUserRequestDto));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUser(userId, userUpdateDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<DeleteResponseDto> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.deleteUser(userId));
    }


    @GetMapping("/login")
    public ResponseEntity<String> getLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user = userPrincipal.getUser();
        loginUsers.put(user.getId(), user);
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }

    @GetMapping("/logout/{userId}")
    public ResponseEntity<String> getLogOut(@PathVariable Long userId) {
        log.info("getLogOut userId: {} ", userId);
        if (!loginUsers.isEmpty() && loginUsers.get(userId) != null) {
            loginUsers.remove(userId);
            return new ResponseEntity<>("User# test@example.com logout successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User# test@example.com is not logged in. Please login to use this functionality...", HttpStatus.OK);
    }


}
