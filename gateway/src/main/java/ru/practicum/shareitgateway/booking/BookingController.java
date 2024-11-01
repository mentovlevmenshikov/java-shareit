package ru.practicum.shareitgateway.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.booking.dto.BookingCreateDto;
import ru.practicum.shareitgateway.booking.dto.BookingUpdateStatusDto;
import ru.practicum.shareitgateway.logging.Logging;

import static ru.practicum.shareitgateway.consts.RequestHeader.HEADER4USER_ID;

@Logging
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createBooking(@RequestHeader(HEADER4USER_ID) long bookerId,
												@RequestBody @Valid BookingCreateDto booking) {
		booking.setBookerId(bookerId);
		return bookingClient.create(booking);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> updateStatus(@PathVariable long bookingId,
								   @RequestHeader(HEADER4USER_ID) long ownerId,
								   @RequestParam boolean approved) {
		BookingUpdateStatusDto booking =  new BookingUpdateStatusDto(bookingId, ownerId, approved);

		return bookingClient.updateStatus(booking);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(HEADER4USER_ID) long userId, @PathVariable long bookingId) {
		return bookingClient.findById(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingsByBookerAndState(@RequestHeader(HEADER4USER_ID) long bookerId,
														@RequestParam(defaultValue = "ALL") BookingState state) {
		return bookingClient.getBookings(bookerId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object>  getBookingsByOwnerAndState(@RequestHeader(HEADER4USER_ID) long ownerId,
													   @RequestParam(defaultValue = "ALL") BookingState state) {
		return bookingClient.getBookingsOwner(ownerId, state);
	}
}
