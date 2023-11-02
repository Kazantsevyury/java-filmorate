package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InMemoryUserStorageTest {

    @Mock
    private GeneratorId generatorId;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private InMemoryUserStorage inMemoryUserStorage;

    @Test
    public void testCreateUserWhenValidUserThenUserAddedToStorageAndIdSet() {
        // Arrange
        User user = new User("login", LocalDate.now());
        when(userValidator.validateUser(user)).thenReturn(true);
        when(generatorId.getNextFreeId()).thenReturn(1);

        // Act
        User result = inMemoryUserStorage.createUser(user);

        // Assert
        assertEquals(1, result.getId());
        verify(userValidator, times(1)).validateUser(user);
        verify(generatorId, times(1)).getNextFreeId();
    }

    @Test
    public void testCreateUserWhenInvalidUserThenValidationExceptionThrown() {
        // Arrange
        User user = new User("login", LocalDate.now());
        when(userValidator.validateUser(user)).thenReturn(false);

        // Act and Assert
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(user));
        verify(userValidator, times(1)).validateUser(user);
    }
}