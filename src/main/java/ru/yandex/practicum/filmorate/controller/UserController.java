package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User save(@RequestBody @Validated User user) {
        log.info("Invoke save method with user = {}", user);
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        log.info("Invoke get method for user with id = {}", id);
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Invoke getAllUsers method");
        List<User> allUsers = new ArrayList<>(users.values());
        log.info("Returning {} users:{}", allUsers.size(), allUsers);
        return allUsers;
    }

    @PutMapping
    public User updateFilm(@RequestBody @Validated User updatedUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException("Validation failed for updated user.");
        }

        if (users.containsKey(updatedUser.getId())) {
            log.info("Invoke updateFilm method for user with id = {}", updatedUser.getId());
            users.remove(updatedUser.getId());
            users.put(updatedUser.getId(), updatedUser);
            return updatedUser;
        } else {
            throw new UserNotFoundException("User with id " + updatedUser.getId() + " not found.");
        }
    }
}