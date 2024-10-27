package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentCreateDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    List<ItemWithBookingDto> getAll4Owner(long ownerId);

    ItemWithBookingDto getById(long id, long userId);

    ItemDto createItem(ItemCreateDto item);

    ItemDto updateItem(ItemUpdateDto item);

    void deleteItem(long id);

    List<ItemDto> searchItems(String searchText);

    CommentDto addComment(CommentCreateDto commentCreateDto);
}