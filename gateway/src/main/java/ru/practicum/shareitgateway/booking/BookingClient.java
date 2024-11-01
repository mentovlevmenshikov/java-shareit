package ru.practicum.shareitgateway.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.booking.dto.BookingCreateDto;
import ru.practicum.shareitgateway.booking.dto.BookingUpdateStatusDto;
import ru.practicum.shareitgateway.client.BaseClient;

import java.util.Map;

@Service
@Slf4j
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(BookingCreateDto requestDto) {
        return post("", requestDto.getBookerId(), requestDto);
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> findById(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }


    public ResponseEntity<Object> getBookingsOwner(long userId, BookingState state) {
        return get("/owner?state={state}", userId);
}

public ResponseEntity<Object> updateStatus(BookingUpdateStatusDto bookingDto) {
    Map<String, Object> parameters = Map.of("approved", bookingDto.getApproved());
    return patch("/" + bookingDto.getBookingId() + "?approved={approved}", bookingDto.getOwnerId(), parameters, null);
}
}