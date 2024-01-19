package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.Status;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NotNull
@ApiModel(description = "User")
public class User {

    private int id;

    @NotBlank
    private final String login;

    private String name;

    @Email
    @ApiModelProperty(value = "Email", example = "tea985@yandex.ru")
    private String email;

    @ApiModelProperty(value = "Date in the format: yyyy-MM-dd", example = "1978-07-19")
    private final LocalDate birthday;

    @JsonIgnore
    private final Set<Integer> idFriends = new HashSet<>();

    @JsonIgnore
    private Status status;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", idFriends=" + idFriends +
                ", status=" + status +
                '}';
    }
}
