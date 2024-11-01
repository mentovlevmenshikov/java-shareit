package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    private User genericUser;
    private UserDto genericUserDto;
    private User genericOwner;
    private Item genericItem;
    private ItemDto genericItemDto;
    private Booking genericBooking;
    private BookingCreateDto genericBookingCreateDto;
    private List<Booking> genericBookings;
    private BookingDto genericBookingResponseDto;
    private static final Sort SORT_BY_START = Sort.by(Sort.Direction.DESC, "start");


    @InjectMocks
    private BookingServiceImpl service;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper mapper;

    @BeforeEach
    public void beforeEach() {
        genericUser = new User();
        genericUser.setId(1L);
        genericUser.setName("name");
        genericUser.setEmail("email@mail.ru");

        genericOwner = new User();
        genericOwner.setId(2L);
        genericOwner.setName("owner");
        genericOwner.setEmail("owner@mail.ru");

        genericUserDto = new UserDto(genericUser.getId(), genericUser.getName(), genericUser.getEmail());

        genericItem = new Item();
        genericItem.setId(1L);
        genericItem.setName("name");
        genericItem.setDescription("description");
        genericItem.setOwner(genericOwner);
        genericItem.setAvailable(true);

        genericItemDto = new ItemDto(1L, "name", "description", true, genericOwner.getId(), 1L);

        genericBookingResponseDto = new BookingDto(1L,  LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2),
                genericItemDto, genericUserDto, BookingStatus.WAITING);

        genericBooking = new Booking();
        genericBooking.setId(1L);
        genericBooking.setItem(genericItem);
        genericBooking.setBooker(genericUser);
        genericBooking.setStart(genericBookingResponseDto.getStart());
        genericBooking.setEnd(genericBookingResponseDto.getEnd());
        genericBooking.setStatus(BookingStatus.WAITING);

        genericBookings = new ArrayList<>();
        genericBookings.add(genericBooking);

        genericBookingCreateDto = new BookingCreateDto(1L, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), 1L);
    }

    @Test
    public void testGetAllBookingsByBooker() {
        List<Booking> foundBookings = new ArrayList<>();
        foundBookings.add(genericBooking);
        List<BookingDto> listToGet = new ArrayList<>();
        listToGet.add(genericBookingResponseDto);

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        when(bookingRepository.findByBookerId(1L, SORT_BY_START))
                .thenReturn(foundBookings);

        when(mapper.toBookingDtoList(foundBookings))
                .thenReturn(listToGet);

        List<BookingDto> result = service.getBookingsByBookerAndState(1L, BookingState.ALL);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetBookingById() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericBooking));

        Mockito
                .when(mapper.toBookingDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        BookingDto result = service.getBooking(1L, 1L);
        Assertions.assertEquals(genericBookingResponseDto, result);
    }

    @Test
    public void testCreateBooking() {
        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericItem));

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        when(bookingRepository.save(genericBooking))
                .thenReturn(genericBooking);

        when(mapper.fromBookingCreateDto(genericBookingCreateDto, genericItem, genericUser))
                .thenReturn(genericBooking);

        when(mapper.toBookingDto(genericBooking))
                .thenReturn(genericBookingResponseDto);

        BookingDto result = service.createBooking(genericBookingCreateDto);
        Assertions.assertEquals(genericBookingResponseDto, result);
    }
}
