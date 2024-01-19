package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Mpa {
    private Integer id;
    private String name;

    public Mpa() {
    }

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
