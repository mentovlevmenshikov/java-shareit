package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentCreateDto {
    private String text;
    private Long authorId;
    private Long itemId;
}
