package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User save(User user);

    User getUserById(int id);

    User updateUser(User user);

    List<User> getAllUsers();

    Map<Integer, User> getMapUsers();
}