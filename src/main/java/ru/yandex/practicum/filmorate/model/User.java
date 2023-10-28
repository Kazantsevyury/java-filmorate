package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@NotNull
@ApiModel(description = "User")
public class User {
    private Long id;

    @Email
    private String email;

    @NotBlank
    private final String login;

    private String name;
    private final LocalDate birthday;

    @JsonIgnore
    private final Set<Long> idFriends = new HashSet<>();
}