package ru.yandex.practicum.filmorate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidUser() throws Exception {
        User validUser = new User(
                "Valid login",
                "Valid User",
                "valid@valid.ru",
                LocalDate.of(2000, 1, 1)
        );

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users/save")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    public void testEmptyEmail() throws Exception {
        User user = new User(
                "Valid login",
                "Valid User",
                "", // Пустая электронная почта
                LocalDate.of(2000, 1, 1)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testEmailWithoutAtSymbol() throws Exception {
        User user = new User(
                "Valid login",
                "Valid User",
                "invalid-email.com",
                LocalDate.of(2000, 1, 1)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testEmptyLogin() throws Exception {
        User user = new User(
                "",
                "Valid User",
                "valid@valid.ru",
                LocalDate.of(2000, 1, 1)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testLoginWithSpaces() throws Exception {
        User user = new User(
                "   ",
                "Valid User",
                "valid@valid.ru",
                LocalDate.of(2000, 1, 1)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testNameEmptyUseLogin() throws Exception {
        User user = new User(
                "Valid login",
                "", // Пустое имя
                "valid@valid.ru",
                LocalDate.of(2000, 1, 1)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFutureBirthday() throws Exception {
        User user = new User(
                "Valid login",
                "Valid User",
                "valid@valid.ru",
                LocalDate.of(2030, 1, 1)
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
