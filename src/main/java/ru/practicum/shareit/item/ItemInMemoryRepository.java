package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemInMemoryRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> itemsByOwner = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<Item> getAll4Owner(long ownerId) {
        List<Item> itemsOfOwner = itemsByOwner.get(ownerId);
        if (itemsOfOwner == null) {
            itemsOfOwner = Collections.emptyList();
        }
        return itemsOfOwner;
    }

    @Override
    public Optional<Item> getById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        itemsByOwner.putIfAbsent(item.getOwnerId(), new ArrayList<>());
        itemsByOwner.get(item.getOwnerId()).add(item);
        return item;
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public void delete(long id) {
        Item item = items.remove(id);
        if (item != null) {
            itemsByOwner.computeIfPresent(item.getOwnerId(), (ownerId, itemsOfOwner) -> {
                itemsOfOwner.remove(item); return itemsOfOwner;
            });
        }
        itemsByOwner.remove(id);
    }

    @Override
    public List<Item> search(String text) {
        String textLowerCase = text.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getDescription().toLowerCase().contains(textLowerCase)
                        || item.getName().toLowerCase().contains(textLowerCase))
                        && item.isAvailable())
                .toList();
    }

    private long getNextId() {
        return nextId++;
    }
}
