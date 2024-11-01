package ru.practicum.shareitgateway.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {

    @NotNull
    private String description;
    private LocalDateTime created;
}
