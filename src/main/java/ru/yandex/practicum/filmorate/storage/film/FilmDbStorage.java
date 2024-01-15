package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectValueException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmValidator filmValidator;

    @Override
    public Film create(Film film) {
        log.info(film.toString());
        SimpleJdbcInsert simpleJdbcInsert;
        if (filmValidator.validatorFilm(film)) {
            log.info(film.toString());
            simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film")
                    .usingGeneratedKeyColumns("film_id");
            film.setId(simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue());
            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update("insert into filmByGenre(film_id, genre_id) values(?, ?)",
                            film.getId(), genre.getId());
                }
            }
        } else {
            throw new ValidationException("User did not pass validation.");
        }
        return getFilm(film.getId());
    }

    @Override
    public Film update(Film film) {
        log.info(film.toString());
        if (filmValidator.validatorFilm(film)) {
            getFilm(film.getId());
            jdbcTemplate.update(
                    "update film set name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa = ?" +
                            " WHERE film_id = ?",
                    film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                    film.getRate() + film.getLikes().size(), film.getMpa().getId(), film.getId());
            if (film.getGenres() != null) {
                if (!film.getGenres().isEmpty()) {
                    jdbcTemplate.update("DELETE FROM filmByGenre WHERE film_id = ?", film.getId());
                    Set<Genre> genreSet = new TreeSet<>(Comparator.comparing(Genre::getId));
                    genreSet.addAll(film.getGenres());
                    for (Genre genre : genreSet) {
                        jdbcTemplate.update("insert into filmByGenre(film_id, genre_id) values (?, ?)",
                                film.getId(), genre.getId());
                    }
                } else {
                    jdbcTemplate.update("DELETE FROM filmByGenre WHERE film_id = ?", film.getId());
                }
            }
        } else {
            throw new ValidationException("id: \" + film.getId() + \" does not exist.");
        }
        return getFilm(film.getId());
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query("SELECT* FROM film JOIN rating ON film.mpa = rating.rating_id",
                (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film getFilm(int id) {
        Film film;
        try {
            film = jdbcTemplate.queryForObject(
                    "SELECT* FROM film JOIN rating ON film.mpa = rating.rating_id WHERE film_id = ?",
                    (rs, rowNum) -> makeFilm(rs), id);
        } catch (RuntimeException e) {
            throw new IncorrectValueException(id);
        }
        return film;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT* FROM rating", (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("rating_id"));
            mpa.setName(rs.getString("rating_name"));
            return mpa;
        });
    }

    @Override
    public Mpa getMpa(int id) {
        filmValidator.validatorMpaId(id);
        return jdbcTemplate.queryForObject("SELECT* FROM rating WHERE rating_id = ?", (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("rating_id"));
            mpa.setName(rs.getString("rating_name"));
            return mpa;
        }, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT* FROM genre", (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        });
    }

    @Override
    public Genre getGenre(int id) {
        filmValidator.validatorGenreId(id);
        return jdbcTemplate.queryForObject("SELECT* FROM genre WHERE genre_id = ?", (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        }, id);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")))
                .genres(idGenres(rs.getInt("film_id")))
                .build();
    }

    private Set<Genre> idGenres(int id) {
        List<Genre> genres = jdbcTemplate.query("SELECT f.genre_id, g.genre_name FROM filmByGenre f " +
                "JOIN genre g ON f.genre_id = g.genre_id WHERE f.film_id = ?", (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        }, id);
        return new LinkedHashSet<>(genres);
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate", film.getRate() + film.getLikes().size());
        values.put("mpa", film.getMpa().getId());
        return values;
    }

    @Override
    public void checkFilmExistence(int id) {
        if (!filmExists(id)) {
            throw new FilmNotFoundException(String.format("Film with id %s does not exist.", id));
        }
    }

    private boolean filmExists(int id) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM film WHERE film_id = ?", Integer.class, id) > 0;
    }

}