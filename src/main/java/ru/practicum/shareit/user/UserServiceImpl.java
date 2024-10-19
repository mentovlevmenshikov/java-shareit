package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.toUserDtos(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(long id) {
        User user = findUserById(id);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        checkDuplicate(userCreateDto.getEmail());
        User user = userMapper.toUserFromUserCreateDto(userCreateDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        User user4Update = findUserById(userUpdateDto.getId());

        if (userUpdateDto.getName() != null) {
            user4Update.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            checkDuplicate(userUpdateDto.getEmail());
            user4Update.setEmail(userUpdateDto.getEmail());
        }
        return userMapper.toUserDto(userRepository.save(user4Update));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    private void checkDuplicate(String  email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            throw new DuplicateException("Username already exists with email " + email);
        }
    }
}
