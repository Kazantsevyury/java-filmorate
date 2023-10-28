package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private final LocalDate birthOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);

    public Film saveFilm(Film film) {
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmById(long id) {
        return filmStorage.getById(id);
    }

    public void deleteFilmById(long id) {
        filmStorage.deleteById(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public String addLikeToFilm(Long id, Long userId) {
        findId(id, userId);
        filmStorage.getMapFilms().get(id).getLikes().add(userId);
        return String.format("The user with id: %s liked the movie with id: %s.", userId, id);
    }

    public List<Film> getTenPopularFilms(Long count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f0, f1) -> compare(f0.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public String removeLikeFromFilm(Long id, Long userId) {
        findId(id, userId);
        filmStorage.getMapFilms().get(id).getLikes().remove(userId);
        return String.format("The user with id: %s removed the like from the movie with id: %s.", userId, id);
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f0, f1) -> compare(f0.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private void findId(Long id, Long userId) {
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