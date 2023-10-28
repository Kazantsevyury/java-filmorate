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

    public User saveUser(User user) {
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void deleteUserById(int id) {
        userStorage.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public String addFriend(int userId, int friendId) {
        findId(userId, friendId);
        userStorage.getMapUsers().get(userId).getIdFriends().add(friendId);
        userStorage.getMapUsers().get(friendId).getIdFriends().add(userId);
        log.info(String.format("Adding a friend with id: %s to the user with id: %s as a friend.", friendId, userId));
        return String.format("The user with id %s has been added as a friend to the user with id %s!", friendId, userId);
    }


    public String deleteFriend(int userId, int friendId) {
        findId(userId, friendId);
        userStorage.getMapUsers().get(userId).getIdFriends().remove(friendId);
        return String.format("The user with id %s has been removed from the friends of the user with id %s!", friendId, userId);
    }

    public List<User> getListOfFriendsSharedWithAnotherUser(int userId, int friendId) {
        findId(userId, friendId);
        List<User> listMutualFriends = new ArrayList<>();

        for (Integer id : userStorage.getUserById(userId).getIdFriends()) {
            if (userStorage.getUserById(friendId).getIdFriends().contains(id)) {
                listMutualFriends.add(userStorage.getUserById(id));
            }
        }
        log.info("Current number of mutual friends: " + userStorage.getMapUsers().size());
        return listMutualFriends;
    }




    public List<User> getListFriendsUser(int id) {
        List<User> allFriends = new ArrayList<>();
        if (!userStorage.getMapUsers().containsKey(id)) {
            throw new UserNotFoundException(String.format("The user with this id: %s does not exist.", id));
        }
        List<Integer> usersId = new ArrayList<>(userStorage.getUserById(id).getIdFriends());
        for (int userid : usersId) {
            allFriends.add(userStorage.getUserById(id));
        }
        log.info("Number of friends: " + allFriends.size());
        return allFriends;
    }


    private void findId(int userId, int friendId) {
        if (!userStorage.getMapUsers().containsKey(userId)) {
            throw new UserNotFoundException(String.format("The user with this id: %s does not exist.", userId));
        }
        if (!userStorage.getMapUsers().containsKey(friendId)) {
            throw new UserNotFoundException(String.format("There is no friend with this id: %s.", friendId));
        }
    }
}