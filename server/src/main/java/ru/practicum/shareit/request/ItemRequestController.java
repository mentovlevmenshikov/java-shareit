package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.logging.Logging;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.Request4ResponseDto;
import static ru.practicum.shareit.consts.RequestHeader.HEADER4USER_ID;

import java.util.List;

@Logging
@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor

public class ItemRequestController {

    private final RequestServiceImpl requestService;

    @PostMapping
    public Request4ResponseDto createRequest(@RequestHeader(HEADER4USER_ID) Long userId,
                                             @RequestBody RequestDto requestDto) {
        return requestService.create(requestDto, userId);
    }

    @GetMapping
    public List<Request4ResponseDto> findAll4User(@RequestHeader(HEADER4USER_ID) Long userId) {
        return requestService.findAll(userId);
    }

    @GetMapping("/all")
    public List<Request4ResponseDto> findAll() {
        return requestService.findAll();

    }

    @GetMapping("/{requestId}")
    public Request4ResponseDto findById(@PathVariable("requestId") long requestId) {
        return requestService.findById(requestId);
    }


}
