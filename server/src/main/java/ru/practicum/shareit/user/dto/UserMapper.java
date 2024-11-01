package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public User toUser(UserDto userDto) {
        User user = new User();
        if (userDto.getName() != null) user.setName(userDto.getName());
        user.setId(userDto.getId());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        return user;
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserCreateDto toUserCreateDto(User user) {
        return new UserCreateDto(null, user.getName(), user.getEmail());
    }

    public User toUserFromUserCreateDto(UserCreateDto userCreateDto) {
        return User.builder()
                .id(userCreateDto.getId())
                .name(userCreateDto.getName())
                .email(userCreateDto.getEmail())
                .build();
    }

    public List<UserDto> toUserDtos(List<User> users) {
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }
}
