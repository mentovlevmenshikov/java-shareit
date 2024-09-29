package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.logging.Logging;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @Logging
    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAll4Owner(userId);
    }

    @Logging
    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable long id) {
        return itemService.getById(id);
    }

    @Logging
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long ownerId, @RequestBody @Valid ItemCreateDto item) {
        item.setOwnerId(ownerId);
        return  itemService.createItem(item);
    }

    @Logging
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId, @RequestBody @Valid ItemUpdateDto itemUpdateDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        itemUpdateDto.setId(itemId);
        itemUpdateDto.setOwnerId(ownerId);
        return itemService.updateItem(itemUpdateDto);
    }

    @Logging
    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
    }

    @Logging
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
       return itemService.searchItems(text);
    }
}
