package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAll4Owner(long ownerId);

    ItemDto getById(long id);

    ItemDto createItem(ItemCreateDto item);

    ItemDto updateItem(ItemUpdateDto item);

    void deleteItem(long id);

    List<ItemDto> searchItems(String searchText);
}
