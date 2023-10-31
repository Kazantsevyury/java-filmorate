package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User createUser(User user);

    User retrieveUserById(int id);

    User updateUser(User user);

    List<User> retrieveAllUsers();

    Map<Integer, User> retrieveUserMap();

    void deleteUserById(int id);

}