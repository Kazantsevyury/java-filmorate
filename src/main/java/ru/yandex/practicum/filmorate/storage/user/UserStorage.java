package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    User createNewUser(@Valid @RequestBody User user);

    User updateUser(@Valid @RequestBody User user);

    List<User> getUsers();

    User findUserById(int id);

    String addFriend(int userId, int friendId);

    String removeFriend(int userId, int friendId);

    Map<Integer, User> getMapUsers();

    void checkUserExistence(int id);
}