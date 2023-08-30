package controller;

import lombok.extern.slf4j.Slf4j;
import model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping("/save")
    public Film  save ( @RequestBody Film film){
        log.info("Invoke save method with film = {}", film);
        films.put(film.getId(),film);
        return films.get(film.getId());
    }
    @GetMapping("/get")
    public Film get(long id){
        log.info("Invoke get method for film with id = {}", id);
        return films.get(id);
    }

    @PutMapping("/updateFilm")
    public Film updateFilm(@PathVariable Long filmId, @RequestBody Film updatedFilm) {
        if (films.containsKey(filmId)) {
            log.info("Invoke updateFilm method for film with id = {}", filmId);
            films.remove(filmId);
            films.put(updatedFilm.getId(),updatedFilm);
            return updatedFilm;
        } else {
            return null;
        }
    }


}
