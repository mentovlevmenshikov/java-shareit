package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.logging.Logging;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userService;

    @Logging
    @GetMapping
    public List<UserDto> getUserAll() {
        return userService.getAllUsers();
    }

    @Logging
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @Logging
    @PostMapping
    public UserDto createUser(@RequestBody @Validated UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @Logging
    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @RequestBody @Validated UserUpdateDto userUpdateDto) {
        userUpdateDto.setId(id);
        return userService.updateUser(userUpdateDto);
    }

    @Logging
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
