package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    public List<UserDto> getAllUsers();

    public UserDto getUserById(long id);

    public UserDto createUser(UserCreateDto userDto);

    public UserDto updateUser(UserUpdateDto userDto);

    public void deleteUser(long id);
}
