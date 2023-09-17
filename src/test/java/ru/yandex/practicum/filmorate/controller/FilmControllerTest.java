package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Logger logger;

    private Film film;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        film = new Film(
                1000L,
                "Lock, Stock and Two Smoking Barrels",
                "A British crime comedy film about friends who get involved in a high-stakes card game.",
                LocalDate.of(1998, 3, 5),
                107,
                2500);
    }

    @Test
    public void testUpdateFilmWhenFilmExistsAndConditionsAreMet() throws Exception {
        when(filmService.existenceOfTheFilmIdInStorage(film.getId())).thenReturn(true);
        when(filmService.conditionsCheck(film)).thenReturn(true);
        when(filmService.saveFilm(film)).thenReturn(film);

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(film.getId()));

        verify(filmService, times(1)).deleteFilmById(film.getId());
        verify(filmService, times(1)).saveFilm(film);
    }

    @Test
    public void testUpdateFilmWhenFilmExistsButConditionsNotMet() throws Exception {
        when(filmService.existenceOfTheFilmIdInStorage(film.getId())).thenReturn(true);
        when(filmService.conditionsCheck(film)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(film)))
                .andExpect(status().isBadRequest());

        verify(filmService, times(0)).saveFilm(film);
    }

    @Test
    public void testSaveFilmWhenFilmDoesNotExistAndConditionsAreMet() throws Exception {
        when(filmService.existenceOfTheFilmIdInStorage(film.getId())).thenReturn(false);
        when(filmService.conditionsCheck(film)).thenReturn(true);
        when(filmService.saveFilm(film)).thenReturn(film);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(film.getId()));

        verify(filmService, times(1)).saveFilm(film);
    }

    @Test
    public void testSaveFilmWhenFilmExistsAndConditionsAreMet() throws Exception {
        when(filmService.existenceOfTheFilmIdInStorage(film.getId())).thenReturn(true);
        when(filmService.conditionsCheck(film)).thenReturn(true);
        when(filmService.saveFilm(film)).thenReturn(film);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(film)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(film.getId()));

        verify(filmService, times(1)).deleteFilmById(film.getId());
        verify(filmService, times(1)).saveFilm(film);
    }

    @Test
    public void testSaveFilmWhenConditionsAreNotMet() throws Exception {
        when(filmService.conditionsCheck(film)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(film)))
                .andExpect(status().isBadRequest());

        verify(filmService, times(0)).saveFilm(film);
    }

    @Test
    public void testGetAllFilms() throws Exception {
        when(filmService.getAllFilms()).thenReturn(Arrays.asList(film));

        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(film.getId()));
    }

    @Test
    public void testGetPopularFilms() throws Exception {
        when(filmService.getPopularFilms(10)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    public void testGetPopularFilmsReturnsCorrectNumberOfFilms() throws Exception {
        // Arrange
        Film film1 = new Film(1L, "Film1", "Description1", null, 120, 100);
        Film film2 = new Film(2L, "Film2", "Description2", null, 120, 200);
        when(filmService.getPopularFilms(2)).thenReturn(Arrays.asList(film1, film2));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/films/popular?count=2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetPopularFilmsReturnsEmptyListWhenNoPopularFilms() throws Exception {
        // Arrange
        when(filmService.getPopularFilms(10)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }
}