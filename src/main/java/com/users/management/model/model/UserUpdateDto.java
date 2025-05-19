package com.users.management.model.model;

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
public class UserUpdateDto {

    private Long id;
    private String name;
    private Set<String> languagesKnow = new HashSet<>();
}
