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
    private final IdGenerator idGenerator = new IdGenerator();
    private final FilmValidator filmValidator;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film save(Film film) {
        if (filmValidator.validatorFilm(film)) {
            int id = idGenerator.getNextFreeId();
            log.info(film.toString());
            film.setId(id);
            films.put(id, film);
        } else {
            throw new ValidationException("The film did not pass validation");
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        if (filmValidator.validatorFilm(film)) {
            if (films.containsKey(film.getId())) {
                log.info(film.toString());
                films.put(film.getId(), film);
            } else {
                throw new ValidationException("id: " + film.getId() + " does not exist.");
            }
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>(films.values());
        log.info("Current number of films: {}", films.size());
        return filmsList;
    }

    @Override
    public Film getById(int id) {
        if (films.containsKey(id)) {
            log.info(films.get(id).toString());
            return films.get(id);
        } else {
            throw new IncorrectValueException(id);
        }
    }

    @Override
    public Map<Integer, Film> getMapFilms() {
        return films;
    }
}
