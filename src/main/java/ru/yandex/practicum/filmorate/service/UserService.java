package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.IdGenerator;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public boolean isIdNull (Long id){
        return id == null;
    }

    public boolean isNameEmptyOrBlank(String name){
        return name.isEmpty()|| name.isBlank();

    }

    public User saveUser(User user) {
        Long id = user.getId();

        if (conditionsCheck(user)) {
            if (isIdNull(id)) {
                user.setId(IdGenerator.generateSimpleUserId());
                log.info("User ID = 0 --> generated neu ID for user with email '{}' and login '{}'", user.getEmail(), user.getLogin());
            }
            if (isNameEmptyOrBlank(user.getName())) {
                user.setName(user.getLogin());
                log.info("Users name is empty --> generated neu Name from login for user with email '{}' and login '{}'", user.getEmail(), user.getLogin());
            }

            User savedUser = userStorage.save(user);
            log.info("User with ID '{}' saved successfully", savedUser.getId());
            return savedUser;
        } else {
            log.error("Failed to save user due to invalid input: {}", user);
            throw new InvalidInputException("Conditions for adding a user are not met");
        }
    }

    public User updateUser(User user) {

        final Long id = user.getId();

        if  ( isIdNull(id) || !(existenceOfTheUserIdInStorage(id))) {
            log.info("Unknow User, or id = null");
            throw new UserNotFoundException("Unknow User, or id = null");
        } else {
            if (conditionsCheck(user)) {
                userStorage.deleteById(user.getId());
                return  userStorage.save(user);
            } else {
                throw new ValidationException("");
            }
        }
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void deleteUserById(Long id) {
        userStorage.deleteById(id);
    }

    public boolean conditionsCheck(User user) {
        Long id = user.getId();
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        LocalDate birthday = user.getBirthday();

        if (!email.isBlank()) {
            log.info("Email is not blank.");

            if (email.contains("@")) {
                log.info("Email contains '@'.");

                if (!login.isBlank() && !login.contains(" ")) {
                    log.info("Login is not blank and does not contain spaces.");

                    if (birthday.isBefore(LocalDate.now())) {
                        log.info("Birthday is before current date.");

                        return true;
                    } else {
                        log.info("Birthday is not before current date.");
                    }
                } else {
                    log.info("Login is blank or contains spaces.");
                }
            } else {
                log.info("Email does not contain '@'.");
            }
        } else {
            log.info("Email is blank.");
        }
        return false;
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
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);

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

    public void deleteLikeFromUser(Long filmId, Long userID) {
        if (existenceOfTheUserIdInStorage(userID)) {
            User user = userStorage.getUserById(userID);
            Set<Long> likes = user.getLikes();

            if (!likes.isEmpty()) {
                likes.remove(filmId);
                user.setLikes(likes);
                userStorage.save(user);
            } else {
                throw new InvalidInputException("The user has not yet put a single like to the movies.");
            }
        }
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

    public boolean existenceOfTheUserIdInStorage(Long id) {
        return userStorage.existenceOfTheUserIdInStorage(id);
    }
}