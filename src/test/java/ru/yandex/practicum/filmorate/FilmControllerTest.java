package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest extends FilmController {
    @Test
    void shouldAddFilm() {
        Film film = new Film(1, "The Shawshank Redemption", "A story of hope and survival inside a maximum security prison",
                LocalDate.of(1994, Month.OCTOBER, 14), 142);
        Film duplicateFilm = new Film(2, "The Shawshank Redemption", "A story of hope and survival inside a maximum security prison",
                LocalDate.of(1994, Month.OCTOBER, 14), 142);

        Assertions.assertEquals(film, createFilm(film));
    }

    @Test
    void shouldNotAddFilmWithDuplicateId() {
        Film film = new Film(1, "The Shawshank Redemption", "A story of hope and survival inside a maximum security prison",
                LocalDate.of(1994, Month.OCTOBER, 14), 142);
        createFilm(film);

        ObjectAlreadyExistException exception = Assertions.assertThrows(ObjectAlreadyExistException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                createFilm(film);
            }
        });
        Assertions.assertEquals("A film with this ID already exists", exception.getMessage());
    }

    @Test
    void shouldNotAddFilmWithDescriptionOver200Characters() {
        Film film = new Film(3, "Pulp Fiction", "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
                LocalDate.of(1994, Month.MAY, 21), 154);

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                film.setDescription("A description that exceeds 200 characters. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eget odio vitae eros commodo viverra. Nulla facilisi. Donec vitae libero eu metus volutpat laoreet eget sed libero. Nullam ut eleifend purus. ");
                createFilm(film);
            }
        });
        Assertions.assertEquals("Conditions for adding a film are not met", exception.getMessage());
    }

    @Test
    void shouldNotAddFilmWithDateBefore1880() {
        Film film = new Film(3, "The Godfather", "An offer you can't refuse.",
                LocalDate.of(1972, Month.MARCH, 14), 175);

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                createFilm(film);
            }
        });
        Assertions.assertEquals("Conditions for adding a film are not met", exception.getMessage());
    }

    @Test
    void shouldNotAddFilmWithZeroDuration() {
        Film film = new Film(3, "The Dark Knight", "Why so serious?",
                LocalDate.of(2008, Month.JULY, 18), 0);

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                createFilm(film);
            }
        });
        Assertions.assertEquals("Conditions for adding a film are not met", exception.getMessage());
    }

    @Test
    void shouldNotAddFilmWithEmptyTitle() {
        Film film = new Film(1, "", "A sample film",
                LocalDate.of(2020, Month.JANUARY, 20), 60);

        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                createFilm(film);
            }
        });
        Assertions.assertEquals("Conditions for adding a film are not met", exception.getMessage());
    }

    @Test
    void shouldRetrieveAllFilms() {
        Film film = new Film(1, "The Shawshank Redemption", "A story of hope and survival inside a maximum security prison",
                LocalDate.of(1994, Month.OCTOBER, 14), 142);
        Film film2 = new Film(2, "The Godfather", "An offer you can't refuse.",
                LocalDate.of(1972, Month.MARCH, 14), 175);
        createFilm(film);
        createFilm(film2);
        ArrayList<Film> expectedFilms = new ArrayList<>();
        expectedFilms.add(film);
        expectedFilms.add(film2);
        ArrayList<Film> values
                = new ArrayList<>(getAllFilms());
        Assertions.assertEquals(2, values.size());
        Assertions.assertEquals(expectedFilms, values);
    }

    @Test
    void shouldUpdateFilm() {
        Film film = new Film(1, "The Shawshank Redemption", "A story of hope and survival inside a maximum security prison",
                LocalDate.of(1994, Month.OCTOBER, 14), 142);
        Film updatedFilm = new Film(1, "Pulp Fiction", "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
                LocalDate.of(1994, Month.MAY, 21), 154);
        createFilm(film);
        Film result = updateFilm(updatedFilm);
        assertEquals(updatedFilm, result);
    }
}