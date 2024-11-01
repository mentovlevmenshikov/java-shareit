package ru.practicum.shareitgateway.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareitgateway.booking.BookingStatus;
import ru.practicum.shareitgateway.item.dto.ItemDto;
import ru.practicum.shareitgateway.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
