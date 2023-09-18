package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film save(Film film);
    Film getById(Long id);
    void deleteById(Long id);
    Collection<Film> getAllFilms();
    boolean existenceOfTheFilmIdInStorage(Long id);
    void updateById(Long id, Film film);
    Collection<Film> getMostLikedFilms(int limit);
    Film addLike(Long userId, Long filmId);
    Collection<Film> getPopularFilms(int count);
    void updateTop();
    List<Film> getTopFilms(int count);
}