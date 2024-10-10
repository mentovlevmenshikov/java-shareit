package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateDto {
    private Long id;
    @NotNull
    private final String name;
    @Email
    @NotNull
    private final String email;
}
