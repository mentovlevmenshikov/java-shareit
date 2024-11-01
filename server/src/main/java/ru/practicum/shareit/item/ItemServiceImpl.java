package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentFullDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private final RequestRepository requestRepository;

    @Override
    public List<ItemWithBookingDto> getAll4Owner(long ownerId) {
        checkUserExists(ownerId);
        List<Item> items = itemRepository.findItemsByOwnerId(ownerId);

        Map<Long, BookingDto> lastBookingsMap = new HashMap<>();
        Map<Long, BookingDto> nextBookingsMap = new HashMap<>();

        List<Booking> orderedBookings =
                bookingRepository.findByItemOwnerIdAndStatusNotOrderByStartDesc(ownerId, BookingStatus.REJECTED);

        LocalDateTime now = LocalDateTime.now();

        for (Item item : items) {
            Long itemId = item.getId();

            orderedBookings.stream()
                    .filter(booking -> booking.getItem().getId().equals(itemId) && booking.getEnd().isBefore(now))
                    .max(Comparator.comparing(Booking::getEnd))
                    .ifPresent(lastBooking -> lastBookingsMap.put(itemId, bookingMapper.toBookingDto(lastBooking)));

            orderedBookings.stream()
                    .filter(booking -> booking.getItem().getId().equals(itemId) && booking.getStart().isAfter(now))
                    .min(Comparator.comparing(Booking::getStart))
                    .ifPresent(nextBooking -> nextBookingsMap.put(itemId, bookingMapper.toBookingDto(nextBooking)));

        }

        List<Comment> comments = commentRepository.findByItem_Owner_Id(ownerId);
        Map<Long, List<CommentFullDto>> commentsMap = comments.stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(commentMapper::toCommentFullDto, Collectors.toList())
                ));

        return items.stream().map(item -> {
            Long itemId = item.getId();
            BookingDto lastBookingDto = lastBookingsMap.get(itemId);
            BookingDto nextBookingDto = nextBookingsMap.get(itemId);
            List<CommentFullDto> itemComments = commentsMap.getOrDefault(itemId, Collections.emptyList());

            return itemMapper.toItemWithBookingDto(item, lastBookingDto, nextBookingDto, itemComments);
        }).collect(Collectors.toList());
    }

    @Override
    public ItemWithBookingDto getById(long id, long userId) {
        Item item = findItemById(id);
        BookingDto bookingLast = null;
        BookingDto bookingNext = null;
        if (Objects.equals(item.getOwner().getId(), userId)) {
            bookingLast = bookingMapper.toBookingDto(bookingRepository.findFirstByItem_IdAndStartBeforeAndStatusNotOrderByStartDesc(id,
                    LocalDateTime.now(), BookingStatus.REJECTED));

            bookingNext = bookingMapper.toBookingDto(bookingRepository.findFirstByItem_IdAndStartAfterAndStatusNotOrderByStart(
                    id, LocalDateTime.now(), BookingStatus.REJECTED));
        }

        List<CommentFullDto> comments = commentRepository.findAllByItemId(id,
                        Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(commentMapper::toCommentFullDto)
                .toList();

        return itemMapper.toItemWithBookingDto(item, bookingLast, bookingNext, comments);
    }

    @Override
    public ItemDto createItem(ItemCreateDto itemCreateDto) {
        User user = checkUserExists(itemCreateDto.getOwnerId());
        Request request = null;
        if (itemCreateDto.getRequestId() != null) {
            request = requestRepository.findById(itemCreateDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Request not found with id " + itemCreateDto.getRequestId()));
        }
        Item item = itemMapper.fromItemCreateDto(itemCreateDto, user, request);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto) {
        Item item4Update = findItemById(itemUpdateDto.getId());
        if (itemUpdateDto.getName() != null) {
            item4Update.setName(itemUpdateDto.getName());
        }

        if (itemUpdateDto.getDescription() != null) {
            item4Update.setDescription(itemUpdateDto.getDescription());
        }

        if (itemUpdateDto.getAvailable() != null) {
            item4Update.setAvailable(itemUpdateDto.getAvailable());
        }

        if (itemUpdateDto.getOwnerId() != null) {
            User user = checkUserExists(itemUpdateDto.getOwnerId());
            item4Update.setOwner(user);
        }

        return itemMapper.toItemDto(itemRepository.save(item4Update));
    }

    @Override
    public void deleteItem(long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> searchItems(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.getItemsBySearchQuery(searchText);
        return itemMapper.toItemDtoList(items);
    }

    @Override
    public CommentDto addComment(CommentCreateDto commentCreateDto) {

        Booking booking = bookingRepository.findFirstByItem_IdAndBooker_IdAndEndIsBefore(commentCreateDto.getItemId(),
                        commentCreateDto.getAuthorId(), LocalDateTime.now().plusSeconds(2))
                .orElseThrow(() -> new ValidationException("Booking is not approved**."));

        Comment comment = commentMapper.fromCommentCreateDto(commentCreateDto, booking.getBooker(), booking.getItem());
        return commentMapper.fromCommentCreateDto(commentRepository.save(comment));
    }

    private Item findItemById(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + id));
    }

    private User checkUserExists(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }
}
