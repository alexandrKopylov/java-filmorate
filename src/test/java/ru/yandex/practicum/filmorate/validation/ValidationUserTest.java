package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUserTest {

    @Test
    public void shouldThrowExceptionWhenEmailIsBlank() {
        User user = new User();
        user.setEmail("   ");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertThrows(ConditionsNotMetValidationException.class, () -> ValidationUser.validation(user));
    }

    @Test
    public void shouldThrowExceptionWhenEmailDoesNotContainAt() {
        User user = new User();
        user.setEmail("example.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertThrows(ConditionsNotMetValidationException.class, () -> ValidationUser.validation(user));
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsBlank() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("   ");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertThrows(LoginValidationException.class, () -> ValidationUser.validation(user));
    }

    @Test
    public void shouldSetNameToLoginWhenNameIsNull() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("mylogin");
        user.setName(null);
        user.setBirthday(LocalDate.now().minusYears(20));

        assertDoesNotThrow(() -> ValidationUser.validation(user));
        assertEquals("mylogin", user.getName());
    }

    @Test
    public void shouldSetNameToLoginWhenNameIsBlank() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("mylogin");
        user.setName("   ");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertDoesNotThrow(() -> ValidationUser.validation(user));
        assertEquals("mylogin", user.getName());
    }

    @Test
    public void shouldThrowExceptionWhenBirthdayInFuture() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(BirthdayValidationException.class, () -> ValidationUser.validation(user));
    }

    @Test
    public void shouldNotThrowExceptionForValidUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("login");
        user.setName("Name");
        user.setBirthday(LocalDate.now().minusYears(20));

        assertDoesNotThrow(() -> ValidationUser.validation(user));
    }
}
