package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(),user);
        return users.get(user.getId());
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public boolean existenceOfTheUserIdInStorage(Long id) {
        return users.containsKey(id);
    }

    @Override
    public Collection<User> getAllUsers(){
        return users.values();
    };

    @Override
    public void updateById (Long id, User user){
        users.remove(id);
        users.put(user.getId(),user);
    };

    @Override
    public User addFriend(Long userId, Long friendId){

        User user = users.get(userId);
        User friend = users.get(friendId);

        if (user != null && friend != null) {
            if (user.getFriends() == null) {
                Set<Long> friends = new HashSet<>();
                friends.add(friendId);
                user.setFriends(friends);
            }
            else {
                Set<Long> friends = user.getFriends();
                friends.add(friendId);
                user.setFriends(friends);
            }

            if (friend.getFriends() == null) {
                Set<Long> friends = new HashSet<>();
                friends.add(userId);
                friend.setFriends(friends);
            }
            else {
                Set<Long> friends = friend.getFriends();
                friends.add(userId);
                friend.setFriends(friends);
            }

            updateById(friendId, friend);
            updateById(userId, user);
            return user;


        } else {
            throw new InvalidInputException("User or friend not found.");
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId){
        User user = users.get(userId);

        Set<Long> userFriends = user.getFriends();
        if (userFriends.contains(friendId)) {
            userFriends.remove(friendId);
            user.setFriends(userFriends);
        }
        updateById(userId, user);
    };

    @Override
    public void addLike (Long userId, Long filmId) {
        User user = users.get(userId);
        if (user.getLikes() == null){
            user.setLikes(Collections.singleton(filmId));
        }
        else {
            Set<Long> likes = new HashSet<>(user.getLikes());
            likes.add(filmId);
            user.setLikes(likes);
        }
        updateById(userId,user);
    };

    @Override
    public void removeLikeFromUser (Long userId, Long filmId){
        User user = users.get(userId);
        Set<Long> likes = new HashSet<>(user.getLikes());
        likes.remove(filmId);
        user.setLikes(likes);
        users.remove(userId);
        users.put(userId,user);
    };

    @Override
        public  Set<Long> getFriendsSet (Long userId){
        return users.get(userId).getFriends();
    };
}