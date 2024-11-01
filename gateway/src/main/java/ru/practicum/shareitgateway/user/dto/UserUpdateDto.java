package ru.practicum.shareitgateway.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDto {
    private Long id;
    private final String name;
    @Email
    private final String email;
}
