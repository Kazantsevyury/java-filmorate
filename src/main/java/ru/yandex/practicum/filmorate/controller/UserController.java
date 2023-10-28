package ru.yandex.practicum.filmorate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserValidator;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "UserController")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    @ApiOperation("Saving a new user to memory.")
    @PostMapping
    public User saveUser(@RequestBody User user) {
        log.info("Created POST request. saveUser");
        return userService.saveUser(user);
    }

    @ApiOperation("Getting a user by Id.")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Created GET request. getUserById");
        return userService.getUserById(id);
    }

    @ApiOperation("Updating a user.")
    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Created PUT request. updateUser");
        return userService.updateUser(user);
    }

    @ApiOperation("Adding a new friend X to user Y.")
    @PutMapping("/{userId}/friends/{friendId}")
    public String addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Created PUT request. addFriend");
        userValidator.validateParameter(userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @ApiOperation("Removing a new friend X to user Y.")
    @DeleteMapping("/{userId}/friends/{friendId}")
    public String deleteFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Created Delete request. deleteFriends");
        userValidator.validateParameter(userId, friendId);
        return userService.deleteFriend(userId, friendId);
    }

    @ApiOperation("Getting a list with all friends from user Y.")
    @GetMapping("/{id}/friends")
    public List<User> getFriendsSet(@PathVariable Long id) {
        log.info("Created Get request. getFriendsSet");
        return userService.getListFriendsUser(id);
    }

    @ApiOperation("Getting a list of friends shared with another user.")
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListOfFriendsSharedWithAnotherUser(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Created Get request. getListOfFriendsSharedWithAnotherUser");
        return userService.getListOfFriendsSharedWithAnotherUser(id, otherId);
    }

    @ApiOperation("Getting a list of all users.")
    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Created GET request. getAllUsers");
        return userService.getAllUsers();
    }

}