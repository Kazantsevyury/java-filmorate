package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public String addFriend(int userId, int friendId) {
        findId(userId, friendId);
        userStorage.getMapUsers().get(userId).getIdFriends().add(friendId);
        userStorage.getMapUsers().get(friendId).getIdFriends().add(userId);
        log.info(String.format("Adding friend with ID: %s to the user with ID: %s as a friend.", friendId, userId));
        return String.format("User with ID %s has been added as a friend to the user with ID %s!", friendId, userId);
    }

    public String removeFriend(int userId, int friendId) {
        findId(userId, friendId);
        userStorage.getMapUsers().get(userId).getIdFriends().remove(friendId);
        return String.format("User with ID %s has been removed from friends of the user with ID %s!", friendId, userId);
    }

    public List<User> listOfMutualFriends(int userId, int friendId) {
        findId(userId, friendId);
        List<User> listMutualFriends = new ArrayList<>();

        for (Integer id : userStorage.getUserById(userId).getIdFriends()) {
            if (userStorage.getUserById(friendId).getIdFriends().contains(id)) {
                listMutualFriends.add(userStorage.getUserById(id));
            }
        }
        log.info("Current count of mutual friends: " + listMutualFriends.size());
        return listMutualFriends;
    }

    public List<User> listFriendsUser(int userId) {
        List<User> allFriends = new ArrayList<>();
        if (!userStorage.getMapUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("User with ID %s does not exist.", userId));
        }
        List<Integer> usersId = new ArrayList<>(userStorage.getUserById(userId).getIdFriends());
        for (Integer id : usersId) {
            allFriends.add(userStorage.getUserById(id));
        }
        log.info("Count of friends: " + allFriends.size());
        return allFriends;
    }

    private void findId(int userId, int friendId) {
        if (!userStorage.getMapUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("User with ID %s does not exist.", userId));
        }
        if (!userStorage.getMapUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Friend with ID %s does not exist.", friendId));
        }
    }
}