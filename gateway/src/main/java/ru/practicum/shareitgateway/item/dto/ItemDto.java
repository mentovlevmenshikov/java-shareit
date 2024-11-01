package ru.practicum.shareitgateway.item.dto;

import lombok.Builder;
import lombok.Data;

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
