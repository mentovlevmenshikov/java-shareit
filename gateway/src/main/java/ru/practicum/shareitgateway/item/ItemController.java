package ru.practicum.shareitgateway.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.item.comment.dto.CommentCreateDto;
import ru.practicum.shareitgateway.item.dto.ItemCreateDto;
import ru.practicum.shareitgateway.item.dto.ItemUpdateDto;
import ru.practicum.shareitgateway.logging.Logging;

import static ru.practicum.shareitgateway.consts.RequestHeader.HEADER4USER_ID;


@Logging
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(HEADER4USER_ID) long userId) {
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader(HEADER4USER_ID) long userId, @PathVariable long id) {
        return itemClient.getItemById(id, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestHeader(HEADER4USER_ID) long ownerId, @RequestBody @Valid ItemCreateDto item) {
        item.setOwnerId(ownerId);
        return  itemClient.create(item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable long itemId, @RequestBody @Valid ItemUpdateDto itemUpdateDto,
                              @RequestHeader(HEADER4USER_ID) Long ownerId) {
        itemUpdateDto.setId(itemId);
        itemUpdateDto.setOwnerId(ownerId);
        return itemClient.update(itemUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable long id) {
        return itemClient.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(HEADER4USER_ID) long userId, @PathVariable long itemId,
                                    @RequestBody @Valid CommentCreateDto commentCreateDto) {
        commentCreateDto.setAuthorId(userId);
        commentCreateDto.setItemId(itemId);
        return itemClient.createComment(commentCreateDto);
    }
}

