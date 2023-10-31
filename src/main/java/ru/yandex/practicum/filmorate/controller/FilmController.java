package ru.yandex.practicum.filmorate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmValidator;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
@Slf4j
@Api(tags = "FilmController", description = "Operations related to films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final FilmValidator filmValidator;

    @ApiOperation("Adding a film")
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        log.info("Adding a film");
        return filmStorage.createFilm(film);
    }

    @ApiOperation("Updating a film")
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating a film");
        return filmStorage.updateFilm(film);
    }

    @ApiOperation("Getting all films")
    @GetMapping()
    public List<Film> getFilms() {
        log.info("Fetching all films");
        return filmStorage.retrieveAllFilms();
    }

    @ApiOperation("Getting a film by ID")
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Integer id) {
        log.info("Fetching film by ID: " + id);
        return filmStorage.retrieveFilmById(id);
    }

    @ApiOperation("Adding a like to a film")
    @PutMapping("{id}/like/{userId}")
    public String addLikeToFilm(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        log.info("Adding a like to film with ID: " + id + " by User ID: " + userId);
        filmValidator.validatorParameter(id, userId);
        return filmService.addLike(id, userId);
    }

    @ApiOperation("Removing a like from a film")
    @DeleteMapping("/{id}/like/{userId}")
    public String removeLikeToFilm(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        log.info("Removing a like from film with ID: " + id + " by User ID: " + userId);
        filmValidator.validatorParameter(id, userId);
        return filmService.removeLike(id, userId);
    }

    @ApiOperation("Getting 10 popular films")
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count < 0) {
            throw new IncorrectValueException(count);
        }
        log.info("Request for " + count + " top films.");
        List<Film> popularFilms = filmService.getTenPopularFilms(count);
        log.info("Returned " + popularFilms.size() + " films");
        return popularFilms;
    }
}