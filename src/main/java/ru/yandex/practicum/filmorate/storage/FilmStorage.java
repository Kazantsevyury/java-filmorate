package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film createFilm(@Valid @RequestBody Film film);

    Film retrieveFilmById(int id);

    Film updateFilm(@Valid @RequestBody Film film);

    List<Film> retrieveAllFilms();

    Map<Integer, Film> retrieveFilmMap();

    void deleteFilmById(int id);

    void deleteAllFilms();
    void checkFilmExistence(int id);
}