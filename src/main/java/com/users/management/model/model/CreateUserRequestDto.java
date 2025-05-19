package com.users.management.model.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Email Id either is empty or not a valid email address")
    private String email;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @JsonIgnore
    private Set<String> languagesKnow = new HashSet<>();
}
