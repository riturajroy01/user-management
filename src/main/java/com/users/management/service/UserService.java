package com.users.management.service;

import com.users.management.exception.NotFoundException;
import com.users.management.model.entity.Users;
import com.users.management.model.model.*;
import com.users.management.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.REPEATABLE_READ)
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        var userEntity = userRepository.save(Users.builder()
                .name(createUserRequestDto.getName())
                .email(createUserRequestDto.getEmail())
                .password(bcryptPasswordEncoder.encode(createUserRequestDto.getPassword()))
                .role("ROLE_USER")
                .build());
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .build();
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.REPEATABLE_READ)
    public UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("UserId# " + userId + " is not a valid input, please enter a valid userId"));

        var name = userUpdateDto.getName().isBlank() ? existingUser.getName() : userUpdateDto.getName();

        Set<String> languagesKnown = existingUser.getLanguagesKnow() == null ? new HashSet<>() : existingUser.getLanguagesKnow();
        languagesKnown.addAll(userUpdateDto.getLanguagesKnow());
        var updatedUser = userRepository.save(Users.builder()
                .id(existingUser.getId())
                .name(name)
                .email(existingUser.getEmail())
                .password(existingUser.getPassword())
                .languagesKnow(languagesKnown)
                .role(existingUser.getRole())
                .build());

        return UserResponseDto.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .languagesKnow(updatedUser.getLanguagesKnow()).build();
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.REPEATABLE_READ)
    public UserResponseDto getUserById(Long userId) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("UserId# " + userId + " is not a valid input, please enter a valid userId"));
        return UserResponseDto.builder()
                .id(existingUser.getId())
                .name(existingUser.getName())
                .email(existingUser.getEmail())
                .languagesKnow(existingUser.getLanguagesKnow()).build();
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.REPEATABLE_READ)
    public DeleteResponseDto deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("UserId#" + userId + " is not a valid input, please enter a valid userId"));

        userRepository.deleteById(userId);
        return DeleteResponseDto.builder()
                .message("UserId#" + userId + " deleted successfully")
                .build();
    }


    public void getUserByEmailAndPassword(LoginRequest loginRequest) {
        Users u = userRepository.findByEmail(loginRequest.getEmail());
        if (u == null || !bcryptPasswordEncoder.matches(loginRequest.getPassword(), bcryptPasswordEncoder.encode(loginRequest.getPassword()))) {
            throw new NotFoundException("UserId# " + loginRequest.getEmail() + " is not available in the system. Please register..");

        }
    }

}

