package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends UserController {

    @Test
    void shouldPutUser() {
        User user = new User(1, "arkady.volozh@yandex.com", "arkady_volozh", "Arkady Volozh",
                LocalDate.of(1964, Month.FEBRUARY, 11));
        Assertions.assertEquals(user, create(user));
    }

    @Test
    void shouldNotPutUserWithUsedId() {
        User user = new User(1, "arkady.volozh@yandex.com", "arkady_volozh", "Arkady Volozh",
                LocalDate.of(1964, Month.FEBRUARY, 11));
        User user2 = new User(1, "arkady.volozh@yandex.com", "arkady_volozh", "Arkady Volozh",
                LocalDate.of(1964, Month.FEBRUARY, 11));
        create(user);
        ObjectAlreadyExistException exp = assertThrows(ObjectAlreadyExistException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                create(user2);
            }
        });
        assertEquals("A user with this ID already exists", exp.getMessage());
    }

    @Test
    void shouldNotPutUserWhenEmailIsWrong() {
        User user = new User(1, "arkady.volozh.yandex.com", "arkady_volozh", "Arkady Volozh",
                LocalDate.of(1964, Month.FEBRUARY, 11));
        InvalidInputException exp = assertThrows(InvalidInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                create(user);
            }
        });
        assertEquals("Conditions for adding a user are not met", exp.getMessage());
    }

    @Test
    void shouldPostUserWithNoId() {
        User user = new User(null, "sasha.lysenko@yandex.com", "sasha_lysenko", "Sasha Lysenko",
                LocalDate.of(1980, Month.AUGUST, 18));
        User expUser = new User(1, "sasha.lysenko@yandex.com", "sasha_lysenko", "Sasha Lysenko",
                LocalDate.of(1980, Month.AUGUST, 18));
        Assertions.assertEquals(expUser, create(user));
    }

    @Test
    void shouldPostUserWithNoId6() {
        User user = new User(1, "sasha.lysenko@yandex.com", "sasha_lysenko", "Sasha Lysenko",
                LocalDate.of(1980, Month.AUGUST, 18));
        User expUser = new User(1, "sasha.lysenko@yandex.com", "sasha_lysenko", "Sasha Lysenko",
                LocalDate.of(1980, Month.AUGUST, 18));
        Assertions.assertEquals(expUser, create(user));
    }

    @Test
    void shouldNotPutUserWhenEmailIsBlank() {
        User user = new User(1, " ", "arkady_volozh", "Arkady Volozh",
                LocalDate.of(1964, Month.FEBRUARY, 11));
        InvalidInputException exp = assertThrows(InvalidInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                create(user);
            }
        });
        assertEquals("Conditions for adding a user are not met", exp.getMessage());
    }

    @Test
    void shouldNotPutUserWhenDateIsNotPast() {
        User user = new User(1, "arkady.volozh@yandex.com", "arkady_volozh", "Arkady Volozh",
                LocalDate.of(2050, Month.FEBRUARY, 11));
        InvalidInputException exp = assertThrows(InvalidInputException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                create(user);
            }
        });
        assertEquals("Conditions for adding a user are not met", exp.getMessage());
    }

    @Test
    void shouldNotPutUserWhenNameIsEmpty() {
        User user = new User(1, "arkady.volozh@yandex.com", "arkady_volozh", null,
                LocalDate.of(1964, Month.FEBRUARY, 11));
        User check = create(user);
        assertEquals("arkady_volozh", check.getName());
    }

    @Test
    void shouldGetAllUsers() {
        User user = new User(1, "arkady.volozh@yandex.com", "arkady_volozh", "Arkady Volozh",
                LocalDate.of(1964, Month.FEBRUARY, 11));
        User user2 = new User(2, "sasha.lysenko@yandex.com", "sasha_lysenko", "Sasha Lysenko",
                LocalDate.of(1980, Month.AUGUST, 18));
        create(user);
        create(user2);
        ArrayList<User> expUsers = new ArrayList<>();
        expUsers.add(user);
        expUsers.add(user2);
        ArrayList<User> values
                = new ArrayList<>(getAllUsers());
        Assertions.assertEquals(2, values.size());
        Assertions.assertEquals(expUsers, values);
    }

    @Test
    void shouldPostAndUpdateUser() {
        User user = new User(1, "arkady.volozh@yandex.com", "arkady_volozh", "Arkady Volozh",
                LocalDate.of(1964, Month.FEBRUARY, 11));
        User user2 = new User(1, "arkady.volozh@yandex.com", "new_arkady_volozh", "New Arkady Volozh",
                LocalDate.of(1964, Month.FEBRUARY, 11));
        create(user);
        User updatedUser = updateUser(user2);
        assertEquals("New Arkady Volozh", updatedUser.getName());
    }
}
