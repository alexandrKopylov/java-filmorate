package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDate;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class User {

    private long id;
    @NonNull
     @EqualsAndHashCode.Include
    private String email;
    @NonNull
    private String login;
    @NonNull
    private String name;
    @NonNull
    private LocalDate birthday;
}
