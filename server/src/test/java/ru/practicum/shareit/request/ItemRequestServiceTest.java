package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.Request4ResponseDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {

    private User genericUser;
    private User genericOwner;
    private final UserDto genericOwnerResponseDto = new UserDto(2L, "owner", "owner@mail.ru");
    private RequestDto genericRequestDto;
    private Item genericItem;
    private ItemDto genericItemResponseDto;
    private Request genericRequest;
    private Request4ResponseDto genericRequestResponseDto;

    @InjectMocks
    private RequestServiceImpl service;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private ItemMapper itemMapper;

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

        genericRequest = new Request();
        genericRequest.setId(1L);
        genericRequest.setDescription("description");
        genericRequest.setUserId(genericUser.getId());
        genericRequest.setCreated(LocalDateTime.now());

        genericItem = new Item();
        genericItem.setId(1L);
        genericItem.setName("name");
        genericItem.setDescription("description");
        genericItem.setOwner(genericOwner);
        genericItem.setAvailable(true);
        genericItem.setRequest(genericRequest);

        genericRequestResponseDto = new Request4ResponseDto(1L, "description", genericRequest.getCreated(), Collections.emptyList());
        genericItemResponseDto = new ItemDto(1L, "name", "description", true, 1L, null);
        genericRequestDto = new RequestDto("decription", null);
    }

    @Test
    public void testGetAllItemRequestsByUserId() {
        List<Request> foundRequests = new ArrayList<>();
        foundRequests.add(genericRequest);
        List<Request4ResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericRequestResponseDto);

        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(genericUser));

        Mockito
                .when(requestMapper.toListRequest4ResponseDto(foundRequests))
                .thenReturn(listToGet);

        when(requestRepository.findAllByUserId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(foundRequests);

        List<Request4ResponseDto> result = service.findAll(1L);
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetAllItemRequests() {
        List<Request> foundRequests = new ArrayList<>();
        foundRequests.add(genericRequest);

        List<Request4ResponseDto> listToGet = new ArrayList<>();
        listToGet.add(genericRequestResponseDto);

        when(requestRepository.findAllByOrderByCreatedDesc())
        .thenReturn(foundRequests);

        Mockito
                .when(requestMapper.toListRequest4ResponseDto(foundRequests))
                .thenReturn(listToGet);

        List<Request4ResponseDto> result = service.findAll();
        Assertions.assertEquals(listToGet, result);
    }

    @Test
    public void testGetItemRequestById() {

        when(requestRepository.findById(1L))
        .thenReturn(Optional.ofNullable(genericRequest));

        when(requestMapper.request4ResponseDto(genericRequest))
                .thenReturn(genericRequestResponseDto);

        Request4ResponseDto result = service.findById(1L);
        Assertions.assertEquals(genericRequestResponseDto, result);
    }

    @Test
    public void testCreateItemRequest() {

        when(requestMapper.toRequest(genericRequestDto))
                        .thenReturn(genericRequest);

        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(genericUser));

        when(requestRepository.save(genericRequest))
                .thenReturn(genericRequest);

        when(requestMapper.request4ResponseDto(genericRequest))
                .thenReturn(genericRequestResponseDto);

        Request4ResponseDto result = service.create(genericRequestDto, 1L);
        Assertions.assertEquals(genericRequestResponseDto, result);
    }
}
