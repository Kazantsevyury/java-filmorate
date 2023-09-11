package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    int idCounter = 1;

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Created POST request");
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        LocalDate birthday = user.getBirthday();
        if (users.containsKey(user.getId())) {
            throw new ObjectAlreadyExistException("A user with this ID already exists");
        }
        if (!email.isBlank() && email.contains("@") && !login.isBlank() &&
                !login.contains(" ") && birthday.isBefore(LocalDate.now())) {
            if (name == null) {
                user.setName(login);
            }
            user.setId(getNextId());
            users.put(user.getId(), user);
        } else {
            throw new InvalidInputException("Conditions for adding a user are not met");
        }
        return user;
    }

    public int getNextId() {
        return idCounter++;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Created PUT request");
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate birthday = user.getBirthday();
        if (!email.isBlank() && email.contains("@") && !login.isBlank() &&
                !login.contains(" ") && birthday.isBefore(LocalDate.now())) {
            if (user.getName().isBlank()) {
                user.setName(login);
            }
            if (users.containsKey(user.getId())) {
                users.remove(user.getId());
                users.put(user.getId(), user);
            } else {
                throw new InvalidInputException("Conditions for updating a user are not met");
            }

        } else {
            throw new InvalidInputException("Conditions for updating a user are not met");
        }
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Created GET request");
        return users.values();
    }
}