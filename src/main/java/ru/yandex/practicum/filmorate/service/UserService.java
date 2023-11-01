package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public String addFriend(int userId, int friendId) {
        userStorage.checkUserExistence(userId);
        userStorage.checkUserExistence(friendId);

        User user = userStorage.retrieveUserById(userId);
        User friend = userStorage.retrieveUserById(friendId);

        user.getIdFriends().add(friendId);
        friend.getIdFriends().add(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        log.info(String.format("Added friend with id: %s to the user with id: %s as a friend.", friendId, userId));
        return String.format("User with id %s has been added as a friend to the user with id %s!", friendId, userId);
    }

    public String removeFriend(int userId, int friendId) {
        userStorage.checkUserExistence(userId);
        userStorage.checkUserExistence(friendId);

        User user = userStorage.retrieveUserById(userId);
        User friend = userStorage.retrieveUserById(friendId);

        user.getIdFriends().remove(friendId);
        friend.getIdFriends().remove(userId);

        userStorage.updateUser(user);
        userStorage.updateUser(friend);

        log.info(String.format("User with id %s removed from friends of user with id %s!", friendId, userId));
        return String.format("User with id %s removed from friends of user with id %s!", friendId, userId);
    }

    public List<User> listOfMutualFriends(int userId, int friendId) {
        userStorage.checkUserExistence(userId);
        userStorage.checkUserExistence(friendId);

        User user = userStorage.retrieveUserById(userId);
        User friend = userStorage.retrieveUserById(friendId);

        List<User> listMutualFriends = new ArrayList<>();

        for (Integer id : user.getIdFriends()) {
            if (friend.getIdFriends().contains(id)) {
                listMutualFriends.add(userStorage.retrieveUserById(id));
            }
        }
        log.info("Current count of mutual friends: " + listMutualFriends.size());
        return listMutualFriends;
    }

    public List<User> listFriendsUser(int userId) {
        userStorage.checkUserExistence(userId);

        User user = userStorage.retrieveUserById(userId);
        List<User> allFriends = new ArrayList<>();

        for (Integer id : user.getIdFriends()) {
            allFriends.add(userStorage.retrieveUserById(id));
        }
        log.info("Number of friends: " + allFriends.size());
        return allFriends;
    }
}
