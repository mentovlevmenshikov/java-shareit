package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingUpdateStatusDto {
    private Long bookingId;
    private Long ownerId;
    private Boolean approved;
}
