package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final TreeMap<Long, Film> filmsTop = new TreeMap<>(
            Collections.reverseOrder()
    );

    @Override
    public Film save(Film film) {
        films.put(film.getId(), film);
        updateTop();
        return films.get(film.getId());
    }

    @Override
    public Film getById(Long id) {
        return films.get(id);
    }

    @Override
    public void deleteById(Long id) {
        films.remove(id);
        updateTop();
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public boolean existenceOfTheFilmIdInStorage(Long id) {
        return films.containsKey(id);
    }

    @Override
    public void updateById(Long id, Film film) {
        films.remove(id);
        films.put(film.getId(), film);
        updateTop();
    }

    @Override
    public Collection<Film> getMostLikedFilms(int limit) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(Film::getLikes).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLike(Long userId, Long filmId) {
        Film film = films.get(filmId);
        film.setId(film.getId() + 1);
        films.remove(filmId);
        films.put(film.getId(), film);
        updateTop();
        return film;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return getTopFilms(count);
    }

    @Override
    public void updateTop() {
        filmsTop.clear();

        for (Film film : films.values()) {
            if (film.getLikes() != 0) {
                filmsTop.put(film.getId(), film);
            }
        }
    }

    @Override
    public List<Film> getTopFilms(int count) {
        updateTop();
        List<Film> topFilms = new ArrayList<>(filmsTop.values());
        if (count >= topFilms.size()) {
            return topFilms;
        }
        return topFilms.subList(0, count);
    }
}