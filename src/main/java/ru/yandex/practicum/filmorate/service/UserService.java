package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.IdGenerator;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User saveUser(User user) {

        Long id = user.getId();
        if (id == null) {
            user.setId(IdGenerator.getUserId());
        }

        if (conditionsCheck(user)) {
            idCheck(user);
            return userStorage.save(user);
        } else {
            throw new InvalidInputException("Conditions for adding a user are not met");
        }
    }

    private User gerUserById(long id) {
        return userStorage.getById(id);
    }

    public void deleteUserById(long id) {
        userStorage.deleteById(id);
    }

    public void idCheck(User user) {
        if (user.getId() == null) {
            user.setId(IdGenerator.getUserId());
        }
    }

    public boolean conditionsCheck(User user) {
        final Long id = user.getId();
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        LocalDate birthday = user.getBirthday();

        return ((!email.isBlank() && email.contains("@") && !login.isBlank() && !login.contains(" ") && birthday.isBefore(LocalDate.now())));

    }

    public User updateUser(User user) {
        Long id = user.getId();
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        if (conditionsCheck(user)) {
            if (userStorage.existenceOfTheUserIdInStorage(id)) {
                userStorage.deleteById(id);
            } else {
                throw new InvalidInputException("Conditions for updating a user are not met");
            }
        } else {
            throw new InvalidInputException("Conditions for updating a user are not met");
        }
        return userStorage.save(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addFriend(Long userId, Long friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.deleteFriend(userId, friendId);
        userStorage.deleteFriend(friendId, userId);
    }

    public Set<Long> getCommonFriends(Long userId1, Long userId2) {
        User user1 = userStorage.getById(userId1);
        User user2 = userStorage.getById(userId2);

        if (user1 != null && user2 != null) {
            Set<Long> commonFriends = new HashSet<>(user1.getFriends());
            commonFriends.retainAll(user2.getFriends());
            return commonFriends;
        } else {
            throw new InvalidInputException("User 1 or user 2 not found.");
        }
    }

    public void addLikesToUser(Long userId, Long filmId) {
        userStorage.addLike(userId, filmId);
    }

    public void removeLikeFromUser(Long userId, Long filmId) {
        userStorage.removeLikeFromUser(userId, filmId);
    }

    public Set<Long> getFriendsSet(Long id) {
        return userStorage.getFriendsSet(id);
    }

    public Set<Long> getListOfFriendsSharedWithAnotherUser(Long id, Long otherId) {
        Set<Long> friendsSet1 = userStorage.getFriendsSet(id);
        Set<Long> friendsSet2 = userStorage.getFriendsSet(otherId);

        Set<Long> sharedFriends = new HashSet<>(friendsSet1);

        sharedFriends.retainAll(friendsSet2);

        return sharedFriends;
    }
}