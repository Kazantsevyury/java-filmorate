package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidator;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmValidator filmValidator;

    private final Map<Long, Film> films = new HashMap<>();
    private final TreeMap<Long, Film> filmsTop = new TreeMap<>(
            Collections.reverseOrder()
    );

    @Override
    public Film save(Film film) {
        if (filmValidator.validatorFilm(film)) {
            Long id = IdGenerator.generateSimpleFilmId();
            film.setId(id);
            films.put(id, film);
        } else {
            throw new ValidationException("User did not pass validation.");
        }
        return films.get(film.getId());
    }

    @Override
    public Film update(Film film) {
        if (filmValidator.validatorFilm(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
            } else {
                throw new ValidationException("id: " + film.getId() + " not exestiert");
            }
        }
        return film;
    }

    @Override
    public Film getById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new IncorrectValueException(id);
        }
    }

    @Override
    public void deleteById(Long id) {
        if (films.containsKey(id)) {
        films.remove(id);
        } else {
                throw new IncorrectValueException(id);
        }
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films1 = new ArrayList<>(films.values());
        return films1;
    }

    @Override
    public Map<Long, Film> getMapFilms() {
        return films;
    }
}