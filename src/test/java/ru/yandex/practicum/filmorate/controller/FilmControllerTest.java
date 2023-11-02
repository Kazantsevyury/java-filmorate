package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.service.FilmValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {

    @Mock
    private FilmStorage filmStorage;

    @Mock
    private FilmService filmService;

    @Mock
    private FilmValidator filmValidator;

    @InjectMocks
    private FilmController filmController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetFilms() {
        // создаем фейковый список фильмов
        List<Film> films = new ArrayList<>();
        LocalDate releaseDate2 = LocalDate.parse("2010-12-10");

        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        films.add(new Film(1,"Film 1","des",releaseDate,23));

        films.add(new Film(2,"Film 2","des",releaseDate2,23));

        // имитируем вызов метода в хранилище
        Mockito.when(filmStorage.retrieveAllFilms()).thenReturn(films);

        // вызываем метод контроллера
        List<Film> result = filmController.getFilms();

        // проверяем, что результат соответствует ожидаемому
        assertEquals(2, result.size());
        assertEquals("Film 1", result.get(0).getName());
        assertEquals("Film 2", result.get(1).getName());
    }

    @Test
    public void testGetFilmById() {
        // создаем фейковый фильм
        LocalDate releaseDate = LocalDate.parse("2010-10-10");
        Film film = new Film(1,"Film 1","des",releaseDate,23);


        // имитируем вызов метода в хранилище
        Mockito.when(filmStorage.retrieveFilmById(1)).thenReturn(film);

        // вызываем метод контроллера
        Film result = filmController.getFilm(1);

        // проверяем, что результат соответствует ожидаемому
        assertEquals("Film 1", result.getName());
    }

    @Test
    public void testAddLikeToFilm() {
        // тестирование лайка фильма
        Mockito.when(filmService.addLike(Mockito.anyInt(), Mockito.anyInt())).thenReturn("Liked");

        String result = filmController.addLikeToFilm(1, 123);

        assertEquals("Liked", result);
    }

    @Test
    public void testRemoveLikeToFilm() {
        // тестирование удаления лайка фильма
        Mockito.when(filmService.removeLike(Mockito.anyInt(), Mockito.anyInt())).thenReturn("Like removed");

        String result = filmController.removeLikeToFilm(1, 123);

        assertEquals("Like removed", result);
    }

}
