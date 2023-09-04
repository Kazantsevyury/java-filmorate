package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
@Data
public class User {
    private static Long idCounter = 0L;

    @NotBlank
    private final String login;

    private final String name;
    private final Long id;

    @Email
    @NotEmpty
    private final String email;

    @Past
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private final LocalDate birthday;

    @JsonCreator
    public User(@JsonProperty("login") String login,
                @JsonProperty("name") String name,
                @JsonProperty("email") String email,
                @JsonProperty("birthday") LocalDate birthday) {
        this.id = ++idCounter;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public User(@JsonProperty("login") String login,
                @JsonProperty("email") String email,
                @JsonProperty("birthday") LocalDate birthday) {
        this.id = ++idCounter;
        this.login = login;
        this.name = login;
        this.email = email;
        this.birthday = birthday;
    }
}
