package ru.practicum.shareitgateway.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.client.BaseClient;
import ru.practicum.shareitgateway.item.comment.dto.CommentCreateDto;
import ru.practicum.shareitgateway.item.dto.ItemCreateDto;
import ru.practicum.shareitgateway.item.dto.ItemUpdateDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getAllItems(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> search(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", null, parameters);
    }

    public ResponseEntity<Object> getItemById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> create(ItemCreateDto itemCreateDto) {
        return post("", itemCreateDto.getOwnerId(), itemCreateDto);
    }

    public ResponseEntity<Object> update(ItemUpdateDto updateDto) {
        return patch("/" + updateDto.getId(), updateDto.getOwnerId(), updateDto);
    }

    public ResponseEntity<Object> delete(long itemId) {
        return delete("/" + itemId);
    }

    public ResponseEntity<Object> createComment(CommentCreateDto createDto) {
        return post("/" + createDto.getItemId() + "/comment", createDto.getAuthorId(), createDto);
    }
}
