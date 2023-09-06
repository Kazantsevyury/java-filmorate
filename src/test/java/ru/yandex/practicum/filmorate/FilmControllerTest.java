package ru.yandex.practicum.filmorate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidReleaseDate() throws Exception {
        Film validFilm = new Film(
                "Valid Film",
                "Description",
                LocalDate.of(2000, 1, 1),
                120
        );

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/films/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    public void testEmptyName() throws Exception {
        Film filmWithEmptyName = new Film(
                "", // Empty name
                "Description",
                LocalDate.of(2000, 1, 1),
                120
        );

        performAndExpectBadRequest(filmWithEmptyName);
    }

    @Test
    public void testDescriptionLength199() throws Exception {
        Film filmWithDescription199 = new Film(
                "Valid Film",
                "Description".repeat(25),
                LocalDate.of(2000, 1, 1),
                120
        );

        performAndExpectOk(filmWithDescription199);
    }

    @Test
    public void testDescriptionLength200() throws Exception {
        Film filmWithDescription200 = new Film(
                "Valid Film",
                "Description".repeat(25),
                LocalDate.of(2000, 1, 1),
                120
        );

        performAndExpectOk(filmWithDescription200);
    }

    @Test
    public void testDescriptionLength201() throws Exception {
        Film filmWithDescription201 = new Film(
                "Valid Film",
                "Description".repeat(25) + "X", // Ожидаем ошибку 201
                LocalDate.of(2000, 1, 1),
                120
        );

        performAndExpectBadRequest(filmWithDescription201);
    }

    @Test
    public void testEmptyDate() throws Exception {
        Film filmWithEmptyDate = new Film(
                "Valid Film",
                "Description",
                null,
                120
        );

        performAndExpectBadRequest(filmWithEmptyDate);
    }

    @Test
    public void testDateBefore1895() throws Exception {
        Film filmWithDateBefore1895 = new Film(
                "Valid Film",
                "Description",
                LocalDate.of(1895, 12, 27),
                120
        );

        performAndExpectBadRequest(filmWithDateBefore1895);
    }

    @Test
    public void testDateInFuture() throws Exception {
        Film filmWithDateInFuture = new Film(
                "Valid Film",
                "Description",
                LocalDate.of(2025, 10, 10),
                120
        );

        performAndExpectOk(filmWithDateInFuture);
    }

    @Test
    public void testDurationZero() throws Exception {
        Film filmWithDurationZero = new Film(
                "Valid Film",
                "Description",
                LocalDate.of(2000, 1, 1),
                0 // Zero duration
        );

        performAndExpectOk(filmWithDurationZero);
    }

    @Test
    public void testNegativeDuration() throws Exception {
        Film filmWithNegativeDuration = new Film(
                "Valid Film",
                "Description",
                LocalDate.of(2000, 1, 1),
                -10
        );

        performAndExpectBadRequest(filmWithNegativeDuration);
    }

    @Test
    public void testDuration160() throws Exception {
        Film filmWithDuration160 = new Film(
                "Valid Film",
                "Description",
                LocalDate.of(2000, 1, 1),
                160
        );

        performAndExpectOk(filmWithDuration160);
    }

    private void performAndExpectOk(Film film) throws Exception {
        performAndExpectStatus(film, org.springframework.http.HttpStatus.OK);
    }

    private void performAndExpectBadRequest(Film film) throws Exception {
        performAndExpectStatus(film, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    private void performAndExpectStatus(Film film, org.springframework.http.HttpStatus status) throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/films/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film)));

        resultActions.andExpect(MockMvcResultMatchers.status().is(status.value()));
    }
}