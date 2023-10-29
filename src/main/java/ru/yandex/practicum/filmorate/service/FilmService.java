package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public String addLike(Integer id, Integer userId) {
        findId(id, userId);
        filmStorage.getMapFilms().get(id).getLikes().add(userId);
        log.info(String.format("User with id: %s liked the film with id: %s", userId, id));
        return String.format("User with id: %s liked the film with id: %s", userId, id);
    }

    public String removeLike(Integer id, Integer userId) {
        findId(id, userId);
        filmStorage.getMapFilms().get(id).getLikes().remove(userId);
        log.info(String.format("User with id: %s removed the like from the film with id: %s", userId, id));
        return String.format("User with id: %s removed the like from the film with id: %s", userId, id);
    }

    public List<Film> getTenPopularFilms(Integer count) {
        log.info("Retrieving the ten most popular films.");
        return filmStorage.getAllFilms().stream()
                .sorted((f0, f1) -> Integer.compare(f1.getLikes().size(), f0.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void findId(Integer id, Integer userId) {
        if (!filmStorage.getMapFilms().containsKey(id)) {
            throw new IncorrectValueException(id);
        }
        if (!userStorage.getMapUsers().containsKey(userId)) {
            throw new IncorrectValueException(id);
        }
    }

    private static int compare(int f0, int f1) {
        return f1 - f0;
    }
}