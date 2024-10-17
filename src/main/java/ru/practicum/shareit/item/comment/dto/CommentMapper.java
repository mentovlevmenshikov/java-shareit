package ru.practicum.shareit.item.comment.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Component
public class CommentMapper {
    public Comment fromCommentCreateDto(CommentCreateDto commentCreateDto, User author, Item item) {
        return Comment.builder()
                .text(commentCreateDto.getText())
                .item(item)
                .author(author)
                .build();
    }

    public CommentDto fromCommentCreateDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .itemId(comment.getItem().getId())
                .created(comment.getCreated())
                .build();
    }

    public CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(comment.getItem())
                .author(comment.getAuthor())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
