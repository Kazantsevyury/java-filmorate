package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public String addLike(Integer id, Integer userId) {
        findId(id, userId);
        filmStorage.retrieveFilmMap().get(id).getLikes().add(userId);
        log.info("User with id: " + userId + " liked the film with id: " + id);
        return String.format("User with id: %s liked the film with id: %s ", userId, id);
    }

    public String removeLike(Integer id, Integer userId) {
        findId(id, userId);
        filmStorage.retrieveFilmMap().get(id).getLikes().remove(userId);
        log.info("User with id: " + userId + " removed the like from the film with id: " + id);
        return String.format("User with id: %s removed the like from the film with id: %s", userId, id);
    }

    public List<Film> getTenPopularFilms(Integer count) {
        List<Film> popularFilms = filmStorage.retrieveAllFilms().stream()
                .sorted((f0, f1) -> compare(f0.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());

        log.info("Retrieved " + popularFilms.size() + " popular films");
        return popularFilms;
    }

    private void findId(Integer id, Integer userId) {
        if (!filmStorage.retrieveFilmMap().containsKey(id)) {
            throw new IncorrectValueException(id);
        }
        if (!userStorage.retrieveUserMap().containsKey(userId)) {
            throw new IncorrectValueException(id);
        }
    }

    private static int compare(int f0, int f1) {
        return f1 - f0;
    }
}