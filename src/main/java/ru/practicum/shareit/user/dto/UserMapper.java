package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
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
