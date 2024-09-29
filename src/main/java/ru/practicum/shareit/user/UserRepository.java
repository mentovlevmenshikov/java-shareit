package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> getById(long id);

    User create(User user);

    User update(User user);

    void delete(long id);

    Optional<User> findByEmail(String email);
}
