package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateStatusDto;
import ru.practicum.shareit.logging.Logging;

import java.util.List;

import static ru.practicum.shareit.consts.RequestHeader.HEADER4USER_ID;

@Logging
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader(HEADER4USER_ID) long bookerId,
                                    @RequestBody BookingCreateDto booking) {
        booking.setBookerId(bookerId);
        return bookingService.createBooking(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@PathVariable long bookingId,
                             @RequestHeader(HEADER4USER_ID) long ownerId,
                             @RequestParam boolean approved) {

        BookingUpdateStatusDto booking =  new BookingUpdateStatusDto(bookingId, ownerId, approved);
        return bookingService.updateStatus(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(HEADER4USER_ID) long userId, @PathVariable long bookingId) {
      return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByBookerAndState(@RequestHeader(HEADER4USER_ID) long bookerId, @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingsByBookerAndState(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerAndState(@RequestHeader(HEADER4USER_ID) long ownerId, @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingsByOwnerAndState(ownerId, state);
    }
}
