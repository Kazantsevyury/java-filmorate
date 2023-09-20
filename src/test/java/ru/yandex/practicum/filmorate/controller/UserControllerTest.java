package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.InvalidInputException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    private User user;

    @Test
    public void testUpdateUserWhenUserIsValidThenReturnsUpdatedUser() {
        // Arrange
        User user = new User(1L, "test@test.com", "test", "Test User", LocalDate.now());
        when(userService.updateUser(user)).thenReturn(user);

        // Act
        User result = userController.updateUser(user);

        // Assert
        assertThat(result).isEqualTo(user);
        verify(userService, times(1)).updateUser(user);
    }

    @BeforeEach
    public void setUp() {
         user = new User(1L, "test@test.com", "test", "Test User", LocalDate.now());
    }

    @Test
    public void testGetAllUsersReturnsListOfUsers() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(user);
        when(userService.getAllUsers()).thenReturn(expectedUsers);

        // Act
        Collection<User> actualUsers = userController.getAllUsers();

        // Assert
        assertThat(actualUsers).isEqualTo(expectedUsers);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetAllUsersReturnsEmptyListWhenNoUsersPresent() {
        // Arrange
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        // Act
        Collection<User> actualUsers = userController.getAllUsers();

        // Assert
        assertThat(actualUsers).isEmpty();
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetAllUsersReturnsCorrectData() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(user);
        when(userService.getAllUsers()).thenReturn(expectedUsers);

        // Act
        Collection<User> actualUsers = userController.getAllUsers();

        // Assert
        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    public void testGetAllUsersCallsServiceMethodOnce() {
        // Arrange
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        // Act
        userController.getAllUsers();

        // Assert
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testUpdateUserWhenGivenValidInputThenReturnUpdatedUser() {
        // Arrange
        Long id = 1L;
        when(userService.updateUser(user)).thenReturn(user);

        // Act
        User result = userController.updateUser(user);

        // Assert
        assertThat(result).isEqualTo(user);
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    public void testUpdateUserWhenGivenInvalidInputThenThrowException() {
        // Arrange
        Long id = 1L;
        when(userService.updateUser(user)).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userController.updateUser(user));
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    public void testGetListOfFriendsSharedWithAnotherUserWhenUserIdsValidThenReturnCorrectSet() {
        // Arrange
        Long userId = 1L;
        Long otherId = 2L;
        Set<Long> sharedFriends = new HashSet<>();
        sharedFriends.add(3L);
        when(userService.getListOfFriendsSharedWithAnotherUser(userId, otherId)).thenReturn(sharedFriends);

        // Act
        Set<Long> result = userController.getListOfFriendsSharedWithAnotherUser(userId, otherId);

        // Assert
        assertThat(result).isEqualTo(sharedFriends);
    }

    @Test
    public void testGetListOfFriendsSharedWithAnotherUserWhenUserIdNonExistentThenReturnNotFound() {
        // Arrange
        Long userId = 1L;
        Long otherId = 2L;
        when(userService.getListOfFriendsSharedWithAnotherUser(userId, otherId)).thenThrow(new InvalidInputException("User not found"));

        // Act & Assert
        assertThatThrownBy(() -> userController.getListOfFriendsSharedWithAnotherUser(userId, otherId))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testSaveUserWhenValidUserThenUserServiceSaveUserCalled() {
        when(userService.saveUser(user)).thenReturn(user);
        User result = userController.saveUser(user);
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testSaveUserWhenValidUserThenReturnSameUser() {
        when(userService.saveUser(user)).thenReturn(user);
        User result = userController.saveUser(user);
        assertThat(result).isEqualTo(user);
    }

    @Test
    public void testAddFriendWhenCalledWithValidIdsThenReturnsUser() {
        Long userId = 1L;
        Long friendId = 2L;
        User dummyUser = new User(1L, "s@a.et", "log", "name", LocalDate.now());
        when(userService.addFriend(userId, friendId)).thenReturn(dummyUser);
        User result = userController.addFriend(userId, friendId);
        assertEquals(dummyUser, result);
        verify(userService, times(1)).addFriend(userId, friendId);
    }

    @Test
    public void testGetFriendsSetWhenUserIdIsValidThenReturnCorrectFriendIds() {
        // Arrange
        Long userId = 1L;
        Set<Long> friendIds = new HashSet<>();
        friendIds.add(2L);
        friendIds.add(3L);
        when(userService.getFriendsSet(userId)).thenReturn(friendIds);

        // Act
        Set<Long> result = userController.getFriendsSet(userId);

        // Assert
        assertThat(result).isEqualTo(friendIds);
    }

    @Test
    public void testGetFriendsSetWhenUserHasNoFriendsThenReturnEmptySet() {
        // Arrange
        Long userId = 1L;
        when(userService.getFriendsSet(userId)).thenReturn(new HashSet<>());

        // Act
        Set<Long> result = userController.getFriendsSet(userId);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void testGetFriendsSetWhenUserIdIsProvidedThenCallUserServiceWithCorrectId() {
        // Arrange
        Long userId = 1L;
        when(userService.getFriendsSet(userId)).thenReturn(new HashSet<>());

        // Act
        userController.getFriendsSet(userId);

        // Assert
        verify(userService, times(1)).getFriendsSet(userId);
    }

    @Test
    public void testDeleteFriendsWhenCalledThenUserServiceDeleteFriendIsCalled() {
        Long userId = 1L;
        Long friendId = 2L;
        userController.deleteFriends(userId, friendId);
        verify(userService, times(1)).deleteFriend(userId, friendId);
    }
}