package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User save(User user);

    User getUserById(Long id);

    void deleteById(Long id);

    User updateUser(User user);

    List<User> getAllUsers();

    Map<Long, User> getMapUsers();
}