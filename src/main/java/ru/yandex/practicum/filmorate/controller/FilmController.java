package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @Autowired
    private final RestTemplate restTemplate;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Created GET request. getAllFilms");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film saveFilm(@RequestBody Film film) {
        log.info("Created POST request. saveFilm");

        filmService.idCheck(film);
        final Long id = film.getId();

        if (filmService.conditionsCheck(film)) {
            if (filmService.existenceOfTheFilmIdInStorage(id)) {
                filmService.deleteFilmById(id);
            }
            return filmService.saveFilm(film);
        } else {
            throw new InvalidInputException("Conditions for adding a film are not met");
        }

    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Created PUT request. updateFilm");

        final Long id = film.getId();

        if (!filmService.existenceOfTheFilmIdInStorage(id)) {
            throw new ObjectAlreadyExistException("A film with this ID is not present");
        }
        
        else {
            if (filmService.conditionsCheck(film)) {
                if (filmService.existenceOfTheFilmIdInStorage(id)) {
                    filmService.deleteFilmById(id);
                }
                return filmService.saveFilm(film);
            } else {
                throw new InvalidInputException("Conditions for adding a film are not met");
            }
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Created Put request.likeFilm");
        filmService.addLikeToFilm(id);

        String url = "http://localhost:8080/users/{userId}/addLikesToUser/{filmId}";
        restTemplate.put(url, null, userId, id);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Created Delete request. deleteLikeFilm");
        filmService.addLikeToFilm(id);

    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("Created GET request. getPopularFilms");
        return filmService.getPopularFilms(count);
    }
}