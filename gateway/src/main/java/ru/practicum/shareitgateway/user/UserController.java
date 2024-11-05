package ru.practicum.shareitgateway.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.logging.Logging;
import ru.practicum.shareitgateway.user.dto.UserCreateDto;
import ru.practicum.shareitgateway.user.dto.UserUpdateDto;

@Logging
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUserAll() {
        return userClient.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object>  getUserById(@PathVariable long id) {
        return userClient.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return userClient.create(userCreateDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody @Valid UserUpdateDto userUpdateDto) {
        userUpdateDto.setId(id);
        return userClient.updateUser(userUpdateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        return userClient.deleteUserById(id);
    }
}
