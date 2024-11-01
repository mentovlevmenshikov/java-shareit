package ru.practicum.shareitgateway.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateDto {
    @NotBlank
    private String text;
    private Long authorId;
    private Long itemId;
}
