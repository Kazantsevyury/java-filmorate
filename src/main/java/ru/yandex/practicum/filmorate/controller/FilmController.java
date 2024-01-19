package ru.yandex.practicum.filmorate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"FilmController"})
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @ApiOperation("Create a film")
    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Creating a new film: ",film.toString());
        return filmStorage.create(film);
    }

    @ApiOperation("Update a film")
    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Updating film: ", film.toString());
        return filmStorage.update(film);
    }

    @ApiOperation("Get all films")
    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Fetching all films");
        return filmStorage.getFilms();
    }

    @ApiOperation("Get a film by ID")
    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Fetching film by ID: {}", id);
        return filmStorage.getFilm(id);
    }

    @ApiOperation("Add a like to a film")
    @PutMapping("/films/{id}/like/{userId}")
    public String addLikeToFilm(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        log.info("Adding a like to film with ID: {} by user with ID: {}", id, userId);
        return filmService.addLike(id, userId);
    }

    @ApiOperation("Remove a like from a film")
    @DeleteMapping("/films/{id}/like/{userId}")
    public String removeLikeFromFilm(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        log.info("Removing a like from film with ID: {} by user with ID: {}", id, userId);
        return filmService.removeLike(id, userId);
    }

    @ApiOperation("Get 10 popular films")
    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count < 0) {
            throw new IncorrectValueException(count);
        }
        log.info("Request for {} top films.", count);
        log.info("{} films returned", filmService.getTenPopularFilms(count).size());
        return filmService.getTenPopularFilms(count);
    }

    @ApiOperation("Get all MPA ratings")
    @GetMapping("/mpa")
    public List<Mpa> getAllMpaRatings() {
        return filmService.getAllMpa();
    }

    @ApiOperation("Get MPA rating by ID")
    @GetMapping("/mpa/{id}")
    public Mpa getMpaRating(@PathVariable Integer id) {
        return filmService.getMpa(id);
    }

    @ApiOperation("Get all genres")
    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @ApiOperation("Get genre by ID")
    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable Integer id) {
        return filmService.getGenre(id);
    }
}