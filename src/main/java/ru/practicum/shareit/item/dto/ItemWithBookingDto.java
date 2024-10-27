package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.dto.CommentFullDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
@Builder
public class ItemWithBookingDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private UserDto owner;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentFullDto> comments;
    private final Long requestId;
}
