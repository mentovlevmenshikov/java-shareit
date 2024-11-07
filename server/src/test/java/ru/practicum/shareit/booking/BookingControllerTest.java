package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.user.dto.UserCreateDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class BookingControllerTest {

    private static final UserCreateDto VALID_USER = new UserCreateDto(null, "name", "email@mail.ru");
    private static final UserCreateDto VALID_BOOKER = new UserCreateDto(null, "booker", "booker@mail.ru");
    private static final ItemCreateDto VALID_ITEM = new ItemCreateDto("name", "description", true, null, null);
    private static final LocalDateTime START = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime END = LocalDateTime.now().plusDays(1).plusHours(1);
    private static final BookingCreateDto VALID_BOOKING = new BookingCreateDto(1L, START, END, 1L);
    private static final String REQUEST_HEADER_USER_ID_TITLE = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetAllBookingsByBooker() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "ALL")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetCurrentBookingsByBooker() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusHours(1), 1L));
        Thread.sleep(2000);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "CURRENT")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetPastBookingsByBooker() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(1).plusNanos(1), 1L));
        Thread.sleep(2000);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "PAST")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetFutureBookingsByBooker() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "FUTURE")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetWaitingBookingsByBooker() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "WAITING")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetRejectedBookingsByBooker() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);
        setBookingStatus(false);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "REJECTED")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("REJECTED"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetBookingsByBookerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "ALL")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetCurrentBookingsByBookerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusHours(1), 1L));
        Thread.sleep(2000);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "CURRENT")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetPastBookingsByBookerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(1).plusNanos(1), 1L));
        Thread.sleep(2000);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "PAST")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetFutureBookingsByBookerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "FUTURE")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetWaitingBookingsByBookerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "WAITING")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetRejectedBookingsByBookerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);
        setBookingStatus(false);

        //when
        mockMvc.perform(
                        get("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("state", "REJECTED")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("REJECTED"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetAllBookingsByOwner() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "ALL")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetCurrentBookingsByOwner() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusHours(1), 1L));
        Thread.sleep(2000);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "CURRENT")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetPastBookingsByOwner() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(1).plusNanos(1), 1L));
        Thread.sleep(2000);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "PAST")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetFutureBookingsByOwner() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "FUTURE")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetWaitingBookingsByOwner() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "WAITING")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetRejectedBookingsByOwner() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);
        setBookingStatus(false);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "REJECTED")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("REJECTED"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetBookingsByOwnerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "ALL")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetCurrentBookingsByOwnerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusHours(1), 1L));
        Thread.sleep(2000);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "CURRENT")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetPastBookingsByOwnerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(1).plusNanos(1), 1L));
        Thread.sleep(2000);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "PAST")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetFutureBookingsByOwnerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "FUTURE")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetWaitingBookingsByOwnerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "WAITING")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andListOnGetRejectedBookingsByOwnerPaged() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);
        setBookingStatus(false);

        //when
        mockMvc.perform(
                        get("/bookings/owner")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("state", "REJECTED")
                                .param("from", "0")
                                .param("size", "1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("REJECTED"))
                .andExpect(jsonPath("$[0].booker.id").value(2))
                .andExpect(jsonPath("$[0].item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andBookingOnGetBookingById() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);


        //when
        mockMvc.perform(
                        get("/bookings/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker.id").value(2))
                .andExpect(jsonPath("$.item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn404OnGetBookingByIdWhenWrongId() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);


        //when
        mockMvc.perform(
                        get("/bookings/99")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                )

                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn201andBookingOnPostBookingWhenValidBooking() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);


        //when
        mockMvc.perform(
                        post("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .content(objectMapper.writeValueAsString(VALID_BOOKING))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker.id").value(2))
                .andExpect(jsonPath("$.item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn404OnPostBookingWhenInvalidBooker() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);


        //when
        mockMvc.perform(
                        post("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .content(objectMapper.writeValueAsString(VALID_BOOKING))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn404OnPostBookingWhenInvalidItem() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidUser(VALID_BOOKER);

        //when
        mockMvc.perform(
                        post("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .content(objectMapper.writeValueAsString(VALID_BOOKING))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn500OnPostBookingWhenUserOwner() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);

        //when
        mockMvc.perform(
                        post("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .content(objectMapper.writeValueAsString(VALID_BOOKING))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn500OnPostBookingWhenUnavailable() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(new ItemCreateDto("name", "description", false, null, null));
        postValidUser(VALID_BOOKER);

        //when
        mockMvc.perform(
                        post("/bookings")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .content(objectMapper.writeValueAsString(VALID_BOOKING))
                                .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andBookingOnPatchBookingWhenApproved() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);


        //when
        mockMvc.perform(
                        patch("/bookings/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("approved", "true")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.booker.id").value(2))
                .andExpect(jsonPath("$.item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andBookingOnPatchBookingWhenRejected() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);


        //when
        mockMvc.perform(
                        patch("/bookings/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                                .param("approved", "false")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.booker.id").value(2))
                .andExpect(jsonPath("$.item.id").value(1));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn500OnPatchBookingWhenUserNotOwner() throws Exception {
        //given
        postValidUser(VALID_USER);
        postValidItem(VALID_ITEM);
        postValidUser(VALID_BOOKER);
        postValidBooking(VALID_BOOKING);


        //when
        mockMvc.perform(
                        patch("/bookings/1")
                                .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                                .param("approved", "true")
                )

                //then
                .andExpect(status().isInternalServerError());
    }

    private void postValidUser(UserCreateDto user) throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidItem(ItemCreateDto item) throws Exception {
        mockMvc.perform(
                post("/items")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidBooking(BookingCreateDto booking) throws Exception {
        mockMvc.perform(
                post("/bookings")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 2)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated());
    }

    private void setBookingStatus(boolean approved) throws Exception {
        mockMvc.perform(
                patch("/bookings/1")
                        .header(REQUEST_HEADER_USER_ID_TITLE, 1)
                        .param("approved", String.valueOf(approved))
        );
    }
}
