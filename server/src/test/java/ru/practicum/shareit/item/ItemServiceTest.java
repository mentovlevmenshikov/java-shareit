package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    private User genericUser;
    private final UserDto genericUserDto = new UserDto(1L, "name", "email@mail.ru");
    private Item genericItem;
    private ItemDto genericItemDto;
    private Request genericItemRequest;
    private Comment genericComment;
    private final CommentDto genericCommentResponseDto = new CommentDto(1L, "text", null, "name",
            null,LocalDateTime.now());

    @InjectMocks
    private ItemServiceImpl service;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private BookingMapper bookingMapper;

    @BeforeEach
    public void beforeEach() {
        genericUser = new User();
        genericUser.setId(1L);
        genericUser.setName("name");
        genericUser.setEmail("email@mail.ru");

        genericItem = new Item();
        genericItem.setId(1L);
        genericItem.setName("name");
        genericItem.setDescription("description");
        genericItem.setOwner(genericUser);
        genericItem.setAvailable(true);

        genericItemDto = new ItemDto(1L, "name", "description", true,
                genericUserDto.getId(), null);

        ItemWithBookingDto genericItemWithBookingDto = new ItemWithBookingDto(1L, "name", "description", true,
                null, null, null, null, null);

        genericItemRequest = new Request();
        genericItemRequest.setId(1L);
        genericItemRequest.setDescription("description");
        genericItemRequest.setUserId(genericUser.getId());
        genericItemRequest.setCreated(LocalDateTime.now());

        genericComment = new Comment();
        genericComment.setId(1L);
        genericComment.setText("text");
        genericComment.setItem(genericItem);
        genericComment.setAuthor(genericUser);
        genericComment.setCreated(genericCommentResponseDto.getCreated());

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
    }

    @Test
    public void testCreateItem() {
        ItemCreateDto itemCreateDto = new ItemCreateDto("name", "description", true,
                1L, null);
        Mockito
                .when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        Mockito
                .when(itemRepository.save(genericItem))
                .thenReturn(genericItem);

        Mockito
                .when(itemMapper.toItemDto(genericItem))
                .thenReturn(genericItemDto);

        Mockito
                .when(itemMapper.fromItemCreateDto(itemCreateDto, genericUser, null))
                .thenReturn(genericItem);

        ItemDto result = service.createItem(itemCreateDto);
        Assertions.assertEquals(genericItemDto, result);
    }

    @Test
    public void testSearchItemByText() {
        List<Item> foundItems = new ArrayList<>();
        foundItems.add(genericItem);
        List<ItemDto> listToGet = new ArrayList<>();
        listToGet.add(genericItemDto);

        Mockito
                .when(itemRepository.getItemsBySearchQuery("search"))
                .thenReturn(foundItems);

        Mockito
                .when(itemMapper.toItemDtoList(foundItems))
                .thenReturn(listToGet);

        List<ItemDto> result = service.searchItems("search");
        Assertions.assertEquals(listToGet, result);
    }
}
