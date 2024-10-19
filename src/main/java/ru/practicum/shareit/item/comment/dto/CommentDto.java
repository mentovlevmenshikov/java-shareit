package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {
    private Long id;
    private String text;
    private Long authorId;
    private String authorName;
    private Long itemId;
    private LocalDateTime created;
}
