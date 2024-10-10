package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAll4Owner(long ownerId);

    Optional<Item> getById(long id);

    Item create(Item item);

    Item update(Item item);

    void delete(long id);

    List<Item> search(String text);
}
