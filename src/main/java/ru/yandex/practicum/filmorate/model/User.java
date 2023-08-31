package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import org.springframework.util.IdGenerator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
@Data
public class User {
    private static Long idCounter = 0L;
    private final Long id;
    @Email
    @NotEmpty
    private final String email;
    @NotBlank
    private final String login;
    private final String name;
    @Past
    private final LocalDate birthday;


    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = ++idCounter;;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
    public User(String email, String login, LocalDate birthday) {
        this.id =  ++idCounter;;
        this.email = email;
        this.login = login;
        this.name = login; // Используем логин как имя по умолчанию
        this.birthday = birthday;
    }
}
