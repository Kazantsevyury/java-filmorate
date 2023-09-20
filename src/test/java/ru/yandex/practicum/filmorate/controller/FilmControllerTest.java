package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FilmController filmController;

    // Existing tests...

    @Test
    public void testGetPopularFilmsReturnsExpectedFilms() {
        // Arrange
        Film film1 = new Film(1L, "Film1", "Description1", LocalDate.now(), 120, 100);
        Film film2 = new Film(2L, "Film2", "Description2", LocalDate.now(), 130, 200);
        List<Film> expectedFilms = new ArrayList<>(Arrays.asList(film1, film2));
        when(filmService.getPopularFilms(2)).thenReturn(expectedFilms);

        // Act
        Collection<Film> actualFilms = filmController.getPopularFilms(2);

        // Assert
        assertEquals(expectedFilms, actualFilms, "The returned films should match the expected films");
    }

    @Test
    public void testGetPopularFilmsReturnsEmptyListWhenCountIsLessThanOne() {
        // Arrange
        when(filmService.getPopularFilms(0)).thenReturn(new ArrayList<>());

        // Act
        Collection<Film> actualFilms = filmController.getPopularFilms(0);

        // Assert
        assertThat(actualFilms).isEmpty();
    }

    @Test
    public void testGetFilmByIdWhenFilmExistsThenReturnFilm() {
        // Arrange
        Long id = 1L;
        Film expectedFilm = new Film(1L, "Film1", "Description1", LocalDate.now(), 120, 100);
        when(filmService.existenceOfTheFilmIdInStorage(id)).thenReturn(true);
        when(filmService.getFilmById(id)).thenReturn(expectedFilm);

        // Act
        Film actualFilm = filmController.getFilmById(id);

        // Assert
        assertEquals(expectedFilm, actualFilm, "The returned film should match the expected film");
    }

    @Test
    public void testGetFilmByIdWhenFilmDoesNotExistThenThrowException() {
        // Arrange
        Long id = 1L;
        when(filmService.existenceOfTheFilmIdInStorage(id)).thenReturn(false);

        // Act and Assert
        assertThrows(FilmNotFoundException.class, () -> filmController.getFilmById(id));
    }

    @Test
    public void testLikeFilmWhenCalledThenAddsLikeAndMakesPutRequest() {
        // Arrange
        Long filmId = 1L;
        Long userId = 1L;

        // Act
        filmController.likeFilm(filmId, userId);

        // Assert
        verify(filmService, times(1)).addLikeToFilm(filmId);
        verify(restTemplate, times(1)).put(anyString(), any(), eq(userId), eq(filmId));
    }

    @Test
    public void testLikeFilmWhenExceptionThrownThenHandlesException() {
        // Arrange
        Long filmId = 1L;
        Long userId = 1L;
        doThrow(new InvalidInputException("Film not found")).when(filmService).addLikeToFilm(filmId);

        // Act and Assert
        try {
            filmController.likeFilm(filmId, userId);
        } catch (InvalidInputException e) {
            assertEquals("Film not found", e.getMessage());
        }
    }

    @Test
    public void testGetAllFilmsWhenCalledThenReturnAllFilms() {
        // Arrange
        Film film1 = new Film(1L, "Film1", "Description1", LocalDate.now(), 120, 100);
        Film film2 = new Film(2L, "Film2", "Description2", LocalDate.now(), 130, 200);
        List<Film> expectedFilms = new ArrayList<>(Arrays.asList(film1, film2)); // Преобразуйте в ArrayList
        when(filmService.getAllFilms()).thenReturn(expectedFilms);

        // Act
        Collection<Film> actualFilms = filmController.getAllFilms();

        // Assert
        assertEquals(expectedFilms, actualFilms, "The returned films should match the expected films");
    }

    @Test
    public void testSaveFilmWhenValidFilmThenCallsFilmServiceSaveFilm() {
        // Arrange
        Film film = new Film();
        when(filmService.saveFilm(film)).thenReturn(film);

        // Act
        filmController.saveFilm(film);

        // Assert
        verify(filmService, times(1)).saveFilm(film);
    }

    @Test
    public void testSaveFilmWhenValidFilmThenReturnsSameFilm() {
        // Arrange
        Film film = new Film();
        when(filmService.saveFilm(film)).thenReturn(film);

        // Act
        Film returnedFilm = filmController.saveFilm(film);

        // Assert
        assertThat(returnedFilm).isEqualTo(film);
    }
}