package ru.practicum.shareitgateway.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareitgateway.item.dto.ItemDto;
import ru.practicum.shareitgateway.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentFullDto {
    private Long id;
    private String text;
    private ItemDto item;
    private UserDto author;
    private String authorName;
    private LocalDateTime created;
}