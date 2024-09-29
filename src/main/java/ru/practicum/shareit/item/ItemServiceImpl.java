package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemDto> getAll4Owner(long ownerId) {
        List<Item> items = itemRepository.getAll4Owner(ownerId);
        return itemMapper.toItemDtoList(items);
    }

    @Override
    public ItemDto getById(long id) {
        Item item = findItemById(id);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto createItem(ItemCreateDto itemCreateDto) {
        checkUserExists(itemCreateDto.getOwnerId());
        Item item = itemMapper.fromItemCreateDto(itemCreateDto);
        return itemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemUpdateDto) {
        Item item4Update = findItemById(itemUpdateDto.getId());
        if (itemUpdateDto.getName() != null) {
            item4Update.setName(itemUpdateDto.getName());
        }

        if (itemUpdateDto.getDescription() != null) {
            item4Update.setDescription(itemUpdateDto.getDescription());
        }

        if (itemUpdateDto.getAvailable() != null) {
            item4Update.setAvailable(itemUpdateDto.getAvailable());
        }

        if (itemUpdateDto.getOwnerId() != null) {
            checkUserExists(itemUpdateDto.getOwnerId());
            item4Update.setOwnerId(itemUpdateDto.getOwnerId());
        }

        return itemMapper.toItemDto(itemRepository.update(item4Update));
    }

    @Override
    public void deleteItem(long id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemDto> searchItems(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.search(searchText);
        return itemMapper.toItemDtoList(items);
    }

    private Item findItemById(long id) {
        return itemRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + id));
    }

    private User checkUserExists(long id) {
        return userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }
}
