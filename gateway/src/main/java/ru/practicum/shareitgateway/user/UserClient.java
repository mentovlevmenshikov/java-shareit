package ru.practicum.shareitgateway.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitgateway.client.BaseClient;
import ru.practicum.shareitgateway.user.dto.UserCreateDto;
import ru.practicum.shareitgateway.user.dto.UserUpdateDto;


@Service
@Slf4j
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(UserCreateDto createDto) {
        return post("", createDto);
    }

    public ResponseEntity<Object> getUserById(long userId) {
        log.info("Get user with id = {}", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> updateUser(UserUpdateDto userUpdateDto) {
        return patch("/" + userUpdateDto.getId(), userUpdateDto);
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> deleteUserById(long userId) {
        return delete("/" + userId);
    }
}