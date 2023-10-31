package ru.yandex.practicum.filmorate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserValidator;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "UserController", description = "Operations related to users")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;
    private final UserValidator userValidator;

    @ApiOperation("Creating a new user")
    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        log.info("Creating a new user");
        return userStorage.createUser(user);
    }

    @ApiOperation("Updating a user")
    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        log.info("Updating a user");
        return userStorage.updateUser(user);
    }

    @ApiOperation("Adding a friend")
    @PutMapping("/{userId}/friends/{friendId}")
    public String addFriend(
            @PathVariable Integer userId,
            @PathVariable Integer friendId
    ) {
        log.info("Adding a friend. User ID: " + userId + ", Friend ID: " + friendId);
        userValidator.validateParameter(userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @ApiOperation("Removing a friend")
    @DeleteMapping("/{userId}/friends/{friendId}")
    public String remove(
            @PathVariable Integer userId,
            @PathVariable Integer friendId
    ) {
        log.info("Removing a friend. User ID: " + userId + ", Friend ID: " + friendId);
        userValidator.validateParameter(userId, friendId);
        return userService.removeFriend(userId, friendId);
    }

    @ApiOperation("Getting all users")
    @GetMapping()
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userStorage.retrieveAllUsers();
    }

    @ApiOperation("Getting a user by ID")
    @GetMapping("/{userId}")
    public User findUserById(@PathVariable Integer userId) {
        log.info("Fetching user by ID: " + userId);
        return userStorage.retrieveUserById(userId);
    }

    @ApiOperation("Getting a list of mutual friends")
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> listOfMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Fetching the list of mutual friends for User ID: " + id + " and Other User ID: " + otherId);
        userValidator.validateParameter(id, otherId);
        return userService.listOfMutualFriends(id, otherId);
    }

    @ApiOperation("Getting a list of users who are friends")
    @GetMapping("/{id}/friends")
    public List<User> listAllFriendUsers(@PathVariable Integer id) {
        if (id < 0) {
            throw new IncorrectParameterException("userId");
        }
        log.info("Fetching all friends of User ID: " + id);
        return userService.listFriendsUser(id);
    }
}
