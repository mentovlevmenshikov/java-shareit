package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.dto.CommentFullDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemMapper {
    private final UserMapper userMapper;

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .ownerId(item.getOwner().getId())
                .requestId(item.getRequest() == null ? null : item.getRequest().getId())
                .build();
    }

    public Item fromItemCreateDto(ItemCreateDto itemCreateDto, User owner, Request request) {
        return Item.builder()
                .name(itemCreateDto.getName())
                .description(itemCreateDto.getDescription())
                .available(itemCreateDto.getAvailable())
                .owner(owner)
                .request(request)
                .build();
    }

    public List<ItemDto> toItemDtoList(List<Item> items) {
        if (items == null) {
            return null;
        }

        return items.stream()
                .map(this::toItemDto)
                .toList();
    }

    public ItemWithBookingDto toItemWithBookingDto(Item item, BookingDto lastBooking, BookingDto nextBooking,
                                                   List<CommentFullDto> comments) {
        return ItemWithBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(userMapper.toUserDto(item.getOwner()))
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }
}
