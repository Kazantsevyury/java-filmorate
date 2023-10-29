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
@Api(tags = "FilmController")
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;
    private final FilmValidator filmValidator;

    @ApiOperation("Saving a new movie to memory.")
    @PostMapping
    public Film saveFilm(@Valid @RequestBody Film film) {
        log.info("Created POST request. saveFilm");
        return filmStorage.save(film);
    }

    @ApiOperation("Updating a saved movie.")
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Created PUT request. updateFilm");
        return filmStorage.update(film);
    }

    @ApiOperation("Getting a list of all movies.")
    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Created GET request. getAllFilms");
        return filmStorage.getAllFilms();
    }

    @ApiOperation("Getting a movie by id.")
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Created GET request. getFilmById");
        return filmStorage.getById(id);
    }

    @ApiOperation("Adding a like from user X to movie Y.")
    @PutMapping("/{id}/like/{userId}")
    public String addLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Created PUT request. likeFilm");
        filmValidator.validatorParameter(id, userId);
        return filmService.addLike(id, userId);

    }

    @ApiOperation("Removing a like from user X to movie Y.")
    @DeleteMapping("/{id}/like/{userId}")
    public String removeLikeFromFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Created DELETE request. deleteLikeFilm");
        filmValidator.validatorParameter(id, userId);
        return filmService.removeLike(id, userId);
    }

    @ApiOperation("Getting a list with TOP movies. ")
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        if (count < 0) {
            throw new IncorrectValueException(count);
        }
        log.info(String.format("Request for %s top movies.", count));
        log.info(String.format("%s movies returned", filmService.getTenPopularFilms(count).size()));
        return filmService.getTenPopularFilms(count);
    }

}