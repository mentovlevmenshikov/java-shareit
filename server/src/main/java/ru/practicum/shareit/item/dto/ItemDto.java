package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ItemDto {
    private final long id;
    private final String name;
    private final String description;
    private final boolean available;
    private final long ownerId;
    private final Long requestId;
}