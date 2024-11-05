package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.Request4ResponseDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    public Request4ResponseDto create(RequestDto requestDto, Long userId) {
        checkUser(userId);
        Request request = requestMapper.toRequest(requestDto);
        request.setUserId(userId);
        Request newRequest = requestRepository.save(request);
        List<Item> items = itemRepository.findAllByRequstIdIn(List.of(newRequest.getId()));
        Request4ResponseDto request4ResponseDto = requestMapper.request4ResponseDto(newRequest);
        request4ResponseDto.setItems(itemMapper.toItemDtoList(items));
        return requestMapper.request4ResponseDto(newRequest);
    }

    public List<Request4ResponseDto> findAll(Long userId) {
        checkUser(userId);
        Sort sort = Sort.by("created").descending();
        List<Request> requests = requestRepository.findAllByUserId(userId, sort);
        return addItemsToRequests(requestMapper.toListRequest4ResponseDto(requests));
    }

    public List<Request4ResponseDto> findAll() {
        List<Request> requests = requestRepository.findAllByOrderByCreatedDesc();
        List<Request4ResponseDto> request4ResponseDtos =  requestMapper.toListRequest4ResponseDto(requests);
        return addItemsToRequests(request4ResponseDtos);
    }

    public Request4ResponseDto findById(long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found with id " + requestId));
        List<Item> items = itemRepository.findAllByRequstIdIn(List.of(request.getId()));
        Request4ResponseDto request4ResponseDto = requestMapper.request4ResponseDto(request);
        request4ResponseDto.setItems(itemMapper.toItemDtoList(items));
        return request4ResponseDto;
    }

    private void checkUser(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id=" + userId));
    }

    private List<Request4ResponseDto> addItemsToRequests(List<Request4ResponseDto> result) {
        List<Long> requestsId = result.stream()
                .map(Request4ResponseDto::getId)
                .collect(toList());

        List<Item> items = itemRepository.findAllByRequstIdIn(requestsId);

        Map<Long, List<ItemDto>> map = new HashMap<>();

        for (ItemDto itemDto : convertItemsToItemDtoList(items)) {
            List<ItemDto> list = map.getOrDefault(itemDto.getRequestId(), new ArrayList<>());
            list.add(itemDto);
            map.put(itemDto.getRequestId(), list);
        }

        for (Request4ResponseDto itemRequestDto : result) {
            itemRequestDto.setItems(map.getOrDefault(itemRequestDto.getId(), emptyList()));
        }

        return result;
    }

    private List<ItemDto> convertItemsToItemDtoList(List<Item> items) {
        return items.stream()
                .map(itemMapper::toItemDto)
                .collect(toList());
    }
}
