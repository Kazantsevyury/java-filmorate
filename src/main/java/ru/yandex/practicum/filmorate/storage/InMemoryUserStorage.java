package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidator;

import java.util.*;

@Component
@AllArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final IdGenerator idGenerator;
    private final UserValidator userValidator;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        if (userValidator.validateUser(user)) {
            if (Objects.isNull(user.getName()) || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            int id = idGenerator.getNextFreeId();
            user.setId(id);
            users.put(id, user);
        } else {
            throw new ValidationException("Пользователь не прошёл валидацию");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userValidator.validateUser(user)) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            } else {
                throw new IncorrectValueException(user.getId());
            }
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new IncorrectValueException(id);
        }
    }

    @Override
    public Map<Integer, User> getMapUsers() {
        return users;
    }
}