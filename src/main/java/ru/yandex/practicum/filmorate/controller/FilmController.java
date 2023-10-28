package ru.yandex.practicum.filmorate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@AllArgsConstructor
@Slf4j
@Api(tags = "FilmController")
public class FilmController {

    private final FilmService filmService;

    @ApiOperation("Getting a list of all movies.")
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Created GET request. getAllFilms");
        return filmService.getAllFilms();
    }

    @ApiOperation("Saving a new movie to memory.")
    @PostMapping
    public Film saveFilm(@RequestBody Film film) {
        log.info("Created POST request. saveFilm");
        return filmService.saveFilm(film);
    }

    @ApiOperation("Updating a saved movie.")
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Created PUT request. updateFilm");
        return filmService.update(film);
    }

    @ApiOperation("Adding a like from user X to movie Y.")
    @PutMapping("/{id}/like/{userId}")
    public String addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Created PUT request. likeFilm");
        return filmService.addLikeToFilm(id, userId);

    }

    @ApiOperation("Removing a like from user X to movie Y.")
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Created DELETE request. deleteLikeFilm");
        filmService.removeLikeFromFilm(id, userId);
    }

    @ApiOperation("Getting a list with TOP movies. ")
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Long count) {
        if (count < 0) {
            throw new IncorrectValueException(count);
        }
        log.info(String.format("Запрос на %s лучших фильмов.", count));
        log.info(String.format("Вернулось %s фильмов", filmService.getTenPopularFilms(count).size()));
        return filmService.getTenPopularFilms(count);
    }

    @ApiOperation("Getting a movie by id.")
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Created GET request. getFilmById");
        return filmService.getFilmById(id);
    }

}