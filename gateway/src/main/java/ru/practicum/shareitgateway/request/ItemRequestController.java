package ru.practicum.shareitgateway.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.logging.Logging;
import ru.practicum.shareitgateway.request.dto.RequestDto;

import static ru.practicum.shareitgateway.consts.RequestHeader.HEADER4USER_ID;

@Logging
@Controller
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor

public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(HEADER4USER_ID) Long userId,
                                                @RequestBody @Valid RequestDto requestDto) {
        return itemRequestClient.create(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll4User(@RequestHeader(HEADER4USER_ID) Long userId) {
        return itemRequestClient.findAll(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        return itemRequestClient.findAll();

    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable("requestId") long requestId) {
        return itemRequestClient.findById(requestId);
    }


}
