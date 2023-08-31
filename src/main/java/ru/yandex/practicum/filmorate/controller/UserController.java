package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping("/save")
    public User save( @RequestBody User user) {
        log.info("Invoke save method with user = {}", user);
        users.put(user.getId(),user);
        return users.get(user.getId());

    }

    @GetMapping("/get/{id}")
    public User get(@PathVariable long id){
        log.info("Invoke get method for film with id = {}", id);
        return users.get(id);
    }

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        log.info("Invoke getAllUsers method");
        List<User> allUsers = new ArrayList<>(users.values());
        log.info("Returning {} users: {}", allUsers.size(), allUsers);
        return allUsers;
    }

    @PutMapping("/updateFilm/{filmId}")
    public User updateFilm(@PathVariable Long userId, @RequestBody User updatedUser) {
        if (users.containsKey(userId)) {
            log.info("Invoke updateFilm method for film with id = {}", userId);
            users.remove(userId);
            users.put(updatedUser.getId(),updatedUser);
            return updatedUser;
        } else {
            return null;
        }
    }


}
