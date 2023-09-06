package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film save(@RequestBody @Valid Film film) {
        log.info("Invoke save method with film = {}", film);
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Film get(@PathVariable long id) {
        log.info("Invoke get method for film with id = {}", id);
        return films.get(id);
    }

    @GetMapping("/get-all")
    public List<Film> getAllFilms() {
        log.info("Invoke getAllFilms method");
        List<Film> allFilms = new ArrayList<>(films.values());
        log.info("Returning {} films: {}", allFilms.size(), allFilms);
        return allFilms;
    }

    @PutMapping("/updateFilm/{filmId}")
    public Film updateFilm(@PathVariable Long filmId, @RequestBody @Valid Film updatedFilm) {
        if (films.containsKey(filmId)) {
            log.info("Invoke updateFilm method for film with id = {}", filmId);
            films.remove(filmId);
            films.put(updatedFilm.getId(), updatedFilm);
            return updatedFilm;
        } else {
            return null;
        }
    }
}
