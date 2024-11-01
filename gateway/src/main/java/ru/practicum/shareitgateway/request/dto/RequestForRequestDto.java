package ru.practicum.shareitgateway.request.dto;

import lombok.Data;
import ru.practicum.shareitgateway.item.dto.ItemDto;
import ru.practicum.shareitgateway.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class RequestForRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private UserDto requestMarker;
    private Set<ItemDto> items;

}
