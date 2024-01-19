package ru.yandex.practicum.filmorate.storage.user;

import lombok.Builder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorId;
import ru.yandex.practicum.filmorate.service.UserValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Component
@Builder
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final GeneratorId generatorId;
    private final UserValidator userValidator;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createNewUser(User user) {
        if (userValidator.validateUser(user)) {
            if (Objects.isNull(user.getName()) || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            int id = generatorId.getNextFreeId();
            user.setId(id);
            users.put(id, user);
            log.info("Created a new user with id: {}", id);
        } else {
            throw new ValidationException("User did not pass validation");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userValidator.validateUser(user)) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Updated user with id: {}", user.getId());
            } else {
                throw new IncorrectValueException(user.getId());
            }
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(int id) {
        if (users.containsKey(id)) {
            log.info("Found user with id: {}", id);
            return users.get(id);
        } else {
            throw new UserNotFoundException(String.format("User with id: %s does not exist.", id));
        }
    }

    @Override
    public String addFriend(int userId, int friendId) {
        findUserById(userId).getIdFriends().add(friendId);
        findUserById(friendId).getIdFriends().add(userId);
        String message = String.format("User with id %s added as a friend to the user with id %s!", friendId, userId);
        log.info(message);
        return message;
    }

    @Override
    public String removeFriend(int userId, int friendId) {
        findUserById(userId).getIdFriends().remove(friendId);
        String message = String.format("User with id %s removed from friends of the user with id %s!", friendId, userId);
        log.info(message);
        return message;
    }

    @Override
    public Map<Integer, User> getMapUsers() {
        log.info("Fetching all users as a map");
        return users;
    }

    @Override
    public void checkUserExistence(int id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.format("User with id %s does not exist.", id));
        }
    }

}