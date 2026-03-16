package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

//тут будет бизнес-логика
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage; //ссылка на хранилище пользователей
    private final Map<Long, Set<Long>> userFriends = new HashMap<>();

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Long id) {
        return userStorage.findById(id);
    }

    //добавляет дружбу между двумя пользователями
    public void addFriend(Long userId, Long friendId) {
        log.debug("Добавление в друзья пользователя с id={} пользователем с id={}", userId, friendId);

        userStorage.findById(userId);
        userStorage.findById(friendId);

        //добавляем друга
        userFriends
                .computeIfAbsent(userId, id -> new HashSet<>())
                .add(friendId);
        //добавляем обратную дружбу
        userFriends
                .computeIfAbsent(friendId, id -> new HashSet<>())
                .add(userId);

        log.info("Пользователь с id {} добавил друга с id {}", userId, friendId);

    }

    //Удаляет дружбу между пользователями
    public void removeFriend(Long userId, Long friendId) {
        log.debug("Удаление из друзей пользователя с id={} пользователя с id={}", userId, friendId);

        userStorage.findById(userId);
        userStorage.findById(friendId);

        Set<Long> friends1 = userFriends.get(userId);

        if (friends1 != null) {
            friends1.remove(friendId);
        }

        Set<Long> friends2 = userFriends.get(friendId);

        if (friends2 != null) {
            friends2.remove(userId);
        }
        log.info("Пользователь с ID {} удалил друга с ID {}", userId, friendId);

    }

    //возвращает общих друзей двух пользователей
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        log.debug("Получение общих друзей с id {} и {}", userId, otherUserId);

        userStorage.findById(userId);
        userStorage.findById(otherUserId);

        Set<Long> friends1 = userFriends.getOrDefault(userId, Collections.emptySet());
        Set<Long> friends2 = userFriends.getOrDefault(otherUserId, Collections.emptySet());

        Set<Long> commonIds = new HashSet<>(friends1);
        commonIds.retainAll(friends2);

        log.info("Список общих друзей у пользователей с id {} и {}: {}", userId, otherUserId, commonIds);
        return commonIds.stream()
                .map(userStorage::findById)
                .toList();
    }

    //получает список друзей пользователя
    public Collection<User> getFriends(Long userId) {
        log.debug("Получение всех друзей пользователя с id={}", userId);

        userStorage.findById(userId);

        Set<Long> friendIds =
                userFriends.getOrDefault(userId, Collections.emptySet());

        log.info("Друзья пользователя с id {}: {}", userId, friendIds);

        return friendIds.stream()
                .map(userStorage::findById)
                .toList();

    }
}
