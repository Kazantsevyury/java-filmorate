package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InMemoryFilmStorageTest {

    @Mock
    private FilmValidator filmValidator;

    @InjectMocks
    private InMemoryFilmStorage inMemoryFilmStorage;

    private Film film;

    @BeforeEach
    public void setUp() {
        film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .build();
    }

    @Test
    public void testCreateFilmWhenFilmIsValidThenFilmIsCreated() {
        // Arrange
        when(filmValidator.validatorFilm(film)).thenReturn(true);

        // Act
        Film createdFilm = inMemoryFilmStorage.createFilm(film);

        // Assert
        assertNotNull(createdFilm);
        assertEquals(film.getName(), createdFilm.getName());
        assertEquals(film.getDescription(), createdFilm.getDescription());
        assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate());
        assertEquals(film.getDuration(), createdFilm.getDuration());
        assertTrue(inMemoryFilmStorage.retrieveFilmMap().containsValue(createdFilm));
    }

    @Test
    public void testCreateFilmWhenFilmIsInvalidThenValidationExceptionIsThrown() {
        // Arrange
        when(filmValidator.validatorFilm(film)).thenReturn(false);

        // Act & Assert
        assertThrows(ValidationException.class, () -> inMemoryFilmStorage.createFilm(film));
        assertFalse(inMemoryFilmStorage.retrieveFilmMap().containsValue(film));
    }
}