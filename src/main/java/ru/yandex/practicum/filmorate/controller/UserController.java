package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User saveUser(@RequestBody User user) {
        log.info("Created POST request. saveUser");
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Created GET request. getUserById");
        if (userService.existenceOfTheUserIdInStorage(id)) {
            return userService.getUserById(id);
        } else {
            throw new UserNotFoundException("User not found.");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Created PUT request. updateUser");

        final Long id = user.getId();

        if (!userService.existenceOfTheUserIdInStorage(id)) {
            throw new ObjectAlreadyExistException("A User with this ID is not present");
        } else {
            if (userService.conditionsCheck(user)) {
                if (userService.existenceOfTheUserIdInStorage(id)) {
                    userService.deleteUserById(id);
                }
                return userService.saveUser(user);
            } else {
                throw new InvalidInputException("Conditions for adding a user are not met");
            }
        }
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Created PUT request. addFriend");
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Created Delete request. deleteFriends");
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<Long> getFriendsSet(@PathVariable Long id) {
        log.info("Created Get request. getFriendsSet");
        return userService.getFriendsSet(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<Long> getListOfFriendsSharedWithAnotherUser(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Created Get request. getListOfFriendsSharedWithAnotherUser");
        return userService.getListOfFriendsSharedWithAnotherUser(id, otherId);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Created GET request. getAllUsers");
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}/addLikesToUser/{filmId}")
    ResponseEntity<Void> addLikesToUser(@PathVariable Long userId, @PathVariable Long filmId) {
        log.info("Created PUT request. addLikesToUser");
        if (userService.existenceOfTheUserIdInStorage(userId)) {
            userService.addLikesToUser(userId, filmId);
            return ResponseEntity.ok().build();
        } else {
            log.error("Error adding likes to user with ID {}: ", userId);
            throw new UserNotFoundException(String.format("User with %s not found", userId));
        }
    }

    @DeleteMapping("/{userId}/deleteLikeFromFilm/{filmId}")
    ResponseEntity<Void> deleteLikesFromUser(@PathVariable Long userId, @PathVariable Long filmId) {
        log.info("Created Delete request. DeleteLikesFromUser");
        if (userService.existenceOfTheUserIdInStorage(userId)) {
            userService.deleteLikeFromUser(filmId, userId);
            return ResponseEntity.ok().build();
        } else {
            log.error("Error delete likes from user with ID {}: ", userId);
            throw new UserNotFoundException(String.format("User with %s not found", userId));
        }
    }
}