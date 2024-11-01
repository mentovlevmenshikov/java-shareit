package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private final User genericUser = new User();
    private final UserDto genericUserResponseDto = new UserDto(1L, "name", "email@mail.ru");
    private final UserCreateDto genericUserCreateDto = new UserCreateDto(null,"name", "email@mail.ru");
    @InjectMocks
    private UserServiceImpl service;

    @Mock
    UserRepository repository;

    @Mock
    UserMapper mapper;

    @BeforeEach
    public void beforeEach() {
        genericUser.setId(1L);
        genericUser.setName("name");
        genericUser.setEmail("email@mail.ru");
    }

    @Test
    public void testGetAllUsers() {
        List<User> foundUsers = new ArrayList<>();
        foundUsers.add(genericUser);
        List<UserDto> listToGet = new ArrayList<>();
        listToGet.add(genericUserResponseDto);

        Mockito
                .when(repository.findAll())
                .thenReturn(foundUsers);

        Mockito
                .when(mapper.toUserDtos(foundUsers))
                .thenReturn(listToGet);

        List<UserDto> receivedList = service.getAllUsers();
        Assertions.assertEquals(listToGet, receivedList);
    }


    @Test
    public void getUserById() {
        Mockito
                .when(repository.findById(1L))
                .thenReturn(Optional.of(genericUser));

        Mockito
                .when(mapper.toUserDto(genericUser))
                .thenReturn(genericUserResponseDto);

        UserDto result = service.getUserById(1L);
        assertEquals(genericUserResponseDto, result);
    }

    @Test
    public void testCreateUser() {
        Mockito
                .when(repository.save(genericUser))
                .thenReturn(genericUser);

        Mockito
                .when(mapper.toUserDto(genericUser))
                .thenReturn(genericUserResponseDto);

        Mockito.
                when(mapper.toUserCreateDto(genericUser))
                .thenReturn(genericUserCreateDto);

        Mockito
                .when(mapper.toUserFromUserCreateDto(genericUserCreateDto))
                .thenReturn(genericUser);


        UserCreateDto createDto = mapper.toUserCreateDto(genericUser);
        createDto.setId(1L);
        UserDto savedUser = service.createUser(createDto);
        assertEquals(genericUserResponseDto, savedUser);
    }

    @Test
    public void testUpdateUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto("updated_name", "updated_email@mail.ru");
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("updated_name");
        updatedUser.setEmail("updated_email@mail.ru");
        UserDto userResponseDto = new UserDto(1L, "updated_name", "updated_email@mail.ru");

        Mockito
                .when(repository.findById(1L))
                .thenReturn(Optional.of(genericUser));

        Mockito
                .when(repository.save(Mockito.any(User.class)))
                .thenReturn(updatedUser);

        Mockito
                .when(mapper.toUserDto(updatedUser))
                .thenReturn(userResponseDto);

        userUpdateDto.setId(1L);
        UserDto userResult = service.updateUser(userUpdateDto);
        assertEquals(userResponseDto, userResult);
    }
}
