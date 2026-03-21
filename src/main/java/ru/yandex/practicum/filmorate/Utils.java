package ru.yandex.practicum.filmorate;

import java.util.Set;

public class Utils {
    public static long getNextId(Set<Long> longList) {
        long currentMaxId = longList
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
