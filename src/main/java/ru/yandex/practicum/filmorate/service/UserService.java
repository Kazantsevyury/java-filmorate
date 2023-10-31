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
        findId(userId, friendId);
        userStorage.retrieveUserMap().get(userId).getIdFriends().add(friendId);
        userStorage.retrieveUserMap().get(friendId).getIdFriends().add(userId);
        log.info(String.format("Added friend with id: %s to the user with id: %s as a friend.", friendId, userId));
        return String.format("User with id %s has been added as a friend to the user with id %s!", friendId, userId);
    }

    public String removeFriend(int userId, int friendId) {
        findId(userId, friendId);
        userStorage.retrieveUserMap().get(userId).getIdFriends().remove(friendId);
        log.info(String.format("User with id %s removed from friends of user with id %s!", friendId, userId));
        return String.format("User with id %s removed from friends of user with id %s!", friendId, userId);
    }

    public List<User> listOfMutualFriends(int userId, int friendId) {
        findId(userId, friendId);
        List<User> listMutualFriends = new ArrayList<>();

        for (Integer id : userStorage.retrieveUserById(userId).getIdFriends()) {
            if (userStorage.retrieveUserById(friendId).getIdFriends().contains(id)) {
                listMutualFriends.add(userStorage.retrieveUserById(id));
            }
        }
        log.info("Current count of mutual friends: " + listMutualFriends.size());
        return listMutualFriends;
    }

    public List<User> listFriendsUser(int userId) {
        List<User> allFriends = new ArrayList<>();
        if (!userStorage.retrieveUserMap().containsKey(userId)) {
            throw new UserNotFoundException(String.format("User with id %s does not exist.", userId));
        }
        List<Integer> usersId = new ArrayList<>(userStorage.retrieveUserById(userId).getIdFriends());
        for (Integer id : usersId) {
            allFriends.add(userStorage.retrieveUserById(id));
        }
        log.info("Number of friends: " + allFriends.size());
        return allFriends;
    }

    private void findId(int userId, int friendId) {
        if (!userStorage.retrieveUserMap().containsKey(userId)) {
            throw new UserNotFoundException(String.format("User with id %s does not exist.", userId));
        }
        if (!userStorage.retrieveUserMap().containsKey(friendId)) {
            throw new UserNotFoundException(String.format("Friend with id %s does not exist.", friendId));
        }
    }
}
