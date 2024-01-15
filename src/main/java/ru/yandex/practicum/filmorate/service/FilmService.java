package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmValidator filmValidator;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public String addLike(Integer id, Integer userId) {
        filmStorage.checkFilmExistence(id);
        userStorage.checkUserExistence(userId);
        Film film = findFilmById(id);
        film.getLikes().add(userId);
        filmStorage.update(film);
        return String.format("User with id: %s added a like to the film with id: %s ", userId, id);
    }

    public String removeLike(Integer id, Integer userId) {
        filmStorage.checkFilmExistence(id);
        userStorage.checkUserExistence(userId);
        Film film = findFilmById(id);
        film.getLikes().remove(userId);
        filmStorage.update(film);
        return String.format("User with id: %s removed a like from the film with id: %s", userId, id);
    }

    public List<Film> getTenPopularFilms(Integer count) {
        return filmStorage.getFilms().stream().sorted((f0, f1) -> compare(f0.getRate(),
                f1.getRate())).limit(count).collect(Collectors.toList());
    }

    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Mpa getMpa(int id) {
        return filmStorage.getMpa(id);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenre(int id) {
        return filmStorage.getGenre(id);
    }

    private Film findFilmById(Integer id) {
        filmValidator.validatorParameter(id);
        Film film = filmStorage.getFilm(id);
        return film;
    }

    private static int compare(int f0, int f1) {
        return f1 - f0; // descending order
    }
}
