package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.apache.coyote.http11.Constants.a;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    int idCounter = 1;
    private final LocalDate birthOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);
    private Film film;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Created GET request");
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Created POST request");
        Integer id = film.getId();
        if (films.containsKey(id)) {
            throw new ObjectAlreadyExistException("A film with this ID already exists");
        }
        if (!film.getName().isBlank() && (film.getDescription().length() <= 200) &&
                (film.getReleaseDate().isAfter(birthOfCinema)) && (film.getDuration() > 0)) {
            if (id == null) {
                film.setId(getNextId());
            }

            films.put(film.getId(), film);

        } else {
            throw new InvalidInputException("Conditions for adding a film are not met");
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Created PUT request");
        if (!films.containsKey(film.getId())) {
            throw new ObjectAlreadyExistException("A film with this ID is not present");
        }
        if (!film.getName().isBlank() && (film.getDescription().length() <= 200) &&
                (film.getReleaseDate().isAfter(birthOfCinema)) && (film.getDuration() > 0)) {
            if (films.containsKey(film.getId())) {
                films.remove(film.getId());
            }
            films.put(film.getId(), film);
        } else {
            throw new InvalidInputException("Conditions for adding a film are not met");
        }
        return film;
    }

    public int getNextId() {
        return idCounter++;
    }
}