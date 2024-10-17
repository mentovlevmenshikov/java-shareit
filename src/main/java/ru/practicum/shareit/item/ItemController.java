package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.logging.Logging;

import java.util.List;

import static ru.practicum.shareit.consts.RequestHeader.HEADER4USER_ID;



@Logging
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemWithBookingDto> getUserItems(@RequestHeader(HEADER4USER_ID) long userId) {
        return itemService.getAll4Owner(userId);
    }

    @GetMapping("/{id}")
    public ItemWithBookingDto getItemById(@RequestHeader(HEADER4USER_ID) long userId, @PathVariable long id) {
        return itemService.getById(id, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader(HEADER4USER_ID) long ownerId, @RequestBody @Valid ItemCreateDto item) {
        item.setOwnerId(ownerId);
        return  itemService.createItem(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestBody @Valid ItemUpdateDto itemUpdateDto,
                              @RequestHeader(HEADER4USER_ID) Long ownerId) {
        itemUpdateDto.setId(itemId);
        itemUpdateDto.setOwnerId(ownerId);
        return itemService.updateItem(itemUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
       return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(HEADER4USER_ID) long userId, @PathVariable long itemId,
                                    @RequestBody CommentCreateDto commentCreateDto) {
        commentCreateDto.setAuthorId(userId);
        commentCreateDto.setItemId(itemId);
        return itemService.addComment(commentCreateDto);
    }
}
