package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    User save(User user);
    User getById(Long id);
    void deleteById(Long id);
    boolean existenceOfTheUserIdInStorage(Long id);
    void updateById(Long id, User user);
    Collection<User> getAllUsers();
    void deleteFriend(Long userId, Long friendId);
    User addFriend(Long userId, Long friendId);
    void addLike(Long userId, Long filmId);
    void removeLikeFromUser(Long userId, Long filmId);
    Set<Long> getFriendsSet(Long userId);
}