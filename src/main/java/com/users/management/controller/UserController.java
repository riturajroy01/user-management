package com.users.management.controller;

import com.users.management.model.model.CreateUserRequestDto;
import com.users.management.model.model.DeleteResponseDto;
import com.users.management.model.model.UserResponseDto;
import com.users.management.model.model.UserUpdateDto;
import com.users.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    /*  @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }*/


}
