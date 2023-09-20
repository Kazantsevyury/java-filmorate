package ru.yandex.practicum.filmorate.storage;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IdGenerator {
    private static Set<Long> filmIds = new HashSet<>();
    private static Set<Long> userIds = new HashSet<>();
    private static Long simleUserId = 0L;
    private static Long simleFilmId = 0L;

    private static Random random = new Random();
    private static int recursionCount = 0;
    private static final int MAX_RECURSION_COUNT = 100;

    public static long getNewFilmId() {
        long randomNumber;
        do {
            randomNumber = Math.abs(random.nextLong()); // Генерировать положительное значение
        } while (filmIds.contains(randomNumber) && recursionCount < MAX_RECURSION_COUNT);

        if (recursionCount >= MAX_RECURSION_COUNT) {
            throw new RuntimeException("Failed to generate a unique ID after 100 attempts.");
        }

        filmIds.add(randomNumber);
        recursionCount = 0;
        return randomNumber;
    }

    public static long getUserId() {
        long randomNumber;
        do {
            randomNumber = Math.abs(random.nextLong()); // Генерировать положительное значение
        } while (userIds.contains(randomNumber) && recursionCount < MAX_RECURSION_COUNT);

        if (recursionCount >= MAX_RECURSION_COUNT) {
            throw new RuntimeException("Failed to generate a unique ID after 100 attempts.");
        }

        userIds.add(randomNumber);
        recursionCount = 0;
        return randomNumber;
    }

    public static long generateSimpleUserId() {
        simleUserId++;
        userIds.add(simleUserId);
        return simleUserId;
    }

    public static long generateSimpleFilmId() {
        simleFilmId++;
        filmIds.add(simleFilmId);
        return simleFilmId;
    }
}