package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingUpdateStatusDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto createBooking(BookingCreateDto bookingCreateDto) {
        checkUserExists(bookingCreateDto.getBookerId());

        Item item = checkItemExists(bookingCreateDto.getItemId());
        if (!item.isAvailable()) {
            throw new ValidationException("Item is not available");
        }

        User booker = checkUserExists(bookingCreateDto.getBookerId());
        if (item.getOwner().getId().equals(booker.getId())) {
            throw new ValidationException("You are already owned");
        }

        Booking booking = bookingMapper.fromBookingCreateDto(bookingCreateDto, item, booker);
        booking.setStatus(BookingStatus.WAITING);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateStatus(BookingUpdateStatusDto bookingUpdateStatusDto) {
        Booking booking4Update = findBookingById(bookingUpdateStatusDto.getBookingId());
        if (!bookingUpdateStatusDto.getOwnerId().equals(booking4Update.getItem().getOwner().getId())) {
            throw new PermissionException("Booking can be approved only by an owner");
        }

        if (bookingUpdateStatusDto.getApproved()) {
            if (booking4Update.getStatus().equals(BookingStatus.APPROVED)) {
                throw new ValidationException("Booking was approved earlier");
            }
            booking4Update.setStatus(BookingStatus.APPROVED);
        } else {
            booking4Update.setStatus(BookingStatus.REJECTED);
        }

        Booking bookingUpdated = bookingRepository.save(booking4Update);
        return bookingMapper.toBookingDto(bookingUpdated);
    }

    @Override
    public BookingDto getBooking(long bookingId, long userId) {
        Booking booking = findBookingById(bookingId);
        if (userId != booking.getItem().getOwner().getId()
                &&  userId != booking.getBooker().getId()) {
            throw new PermissionException("User are not owner and are not booker");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBookerAndState(long bookerId, BookingState state) {
        checkUserExists(bookerId);
        List<Booking> bookings = Collections.emptyList();

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBookerId(bookerId, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(bookerId, LocalDateTime.now(),
                        LocalDateTime.now(), sort);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(bookerId, LocalDateTime.now(), sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatus(bookerId, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, sort);
                break;
        }

        return bookingMapper.toBookingDtoList(bookings);
    }

    @Override
    public List<BookingDto> getBookingsByOwnerAndState(long ownerId, BookingState state) {
        checkUserExists(ownerId);
        List<Booking> bookings = Collections.emptyList();
        Sort sort = Sort.by(Sort.Direction.DESC, "startDate");
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItem_Owner_Id(ownerId, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(ownerId, LocalDateTime.now(),
                        LocalDateTime.now(), sort);
                break;
            case PAST:
                bookings = bookingRepository.findByItem_Owner_IdAndEndIsBefore(ownerId, LocalDateTime.now(), sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItem_Owner_IdAndStartIsAfter(ownerId, LocalDateTime.now(),
                        sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(ownerId, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItem_Owner_IdAndStatus(ownerId, BookingStatus.REJECTED, sort);
                break;
        }

        return bookingMapper.toBookingDtoList(bookings);
    }

    private Booking findBookingById(long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with id " + id));
    }

    private Item checkItemExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + itemId));
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id " + userId));
    }
}
