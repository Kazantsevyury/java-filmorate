package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film save(@Valid @RequestBody Film film);

    Film update(@Valid @RequestBody Film film);

    Film getById(Long id);

    void deleteById(Long id);

    List<Film> getAllFilms();

    Map<Long, Film> getMapFilms();
}