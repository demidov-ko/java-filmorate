/**
 * Пакет контроллеров приложения Filmorate.
 */
package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    //создание нового пользователя
    public User create(@RequestBody final User user) {
        log.info("Создание нового пользователя: {}", user.getLogin());

        // Проверка email: не null, не пустой, содержит '@'
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Попытка создать пользователя с не указанным email");
            throw new ValidationException("email не указан");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Попытка создать пользователя с некорректным email: {}", user.getEmail());
            throw new ValidationException("email должен содержать символ '@'");
        }

        // Проверяем уникальность email
        boolean emailExists = users.values().stream()
                .anyMatch(existingUser -> user.getEmail().equals(existingUser.getEmail()));
        if (emailExists) {
            log.warn("Попытка создать пользователя с уже существующим email: {}", user.getEmail());
            throw new DuplicatedDataException("Этот email уже используется");
        }

        // Проверка логина: не null, не пустой, не содержит пробелов
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Попытка создать пользователя с не указанным login");
            throw new ValidationException("login должен быть указан");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Попытка создать пользователя с login, содержащим пробелы: {}", user.getLogin());
            throw new ValidationException("login не может содержать пробелы");
        }

        // Проверяем уникальность логина
        boolean loginExists = users.values().stream()
                .anyMatch(existingUser -> user.getLogin().equals(existingUser.getLogin()));
        if (loginExists) {
            log.warn("Попытка создать пользователя с уже существующим login: {}", user.getLogin());
            throw new DuplicatedDataException("Этот login уже используется");
        }

        // Имя может быть пустым — тогда используем логин
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.debug("Имя не указано, установлено значение: {}", user.getName());
        }

        // Проверка даты рождения: не null и не в будущем
        if (user.getBirthday() == null) {
            log.warn("Попытка создать пользователя без указания даты рождения");
            throw new ValidationException("Дата рождения должна быть указана");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Попытка создать пользователя с датой рождения в будущем: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        user.setId(getNextId());
        users.put(user.getId(), user);

        log.debug("Пользователь {} успешно создан с ID: {}", user.getLogin(), user.getId());

        return user;
    }

    @PutMapping
    //редактируем пользователя
    public User update(@RequestBody final User newUser) {
        log.info("Редактирование пользователя ID={}: {}", newUser.getId(), newUser.getLogin());

        if (newUser.getId() == null) {
            log.warn("Попытка обновить пользователя без id");
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            log.warn("Попытка найти пользователя с несуществующим id: {}", newUser.getId());
            throw new NotFoundException("Пользователь с id " + newUser.getId() + " не найден");
        }
        User oldUser = users.get(newUser.getId());

        //проверяем уникальность email, если он указан и отличается от текущего
        if (newUser.getEmail() != null && !newUser.getEmail().equals(oldUser.getEmail())) {
            // Проверка формата email
            if (newUser.getEmail().isBlank()) {
                log.warn("Попытка обновить email на пустой");
                throw new ValidationException("email не может быть пустым");
            }
            if (!newUser.getEmail().contains("@")) {
                log.warn("Попытка обновить email на некорректный: {}", newUser.getEmail());
                throw new ValidationException("email должен содержать символ '@'");
            }
            // Проверка уникальности email
            boolean emailExists = users.values().stream()
                    .anyMatch(user -> newUser.getEmail().equals(user.getEmail()));
            if (emailExists) {
                log.warn("Попытка обновить email на уже используемый: {}", newUser.getEmail());
                throw new DuplicatedDataException("Этот email уже используется");
            }
            oldUser.setEmail(newUser.getEmail());
            log.debug("Email пользователя ID={} обновлён на: {}", oldUser.getId(), newUser.getEmail());
        }

        // Проверка уникальности логина, если он указан и отличается от текущего
        if (newUser.getLogin() != null && !newUser.getLogin().equals(oldUser.getLogin())) {
            // Проверка логина при обновлении
            if (newUser.getLogin().isBlank()) {
                log.warn("Попытка обновить login на пустой");
                throw new ValidationException("login должен быть указан");
            }
            if (newUser.getLogin().contains(" ")) {
                log.warn("Попытка обновить login на значение с пробелами: {}", newUser.getLogin());
                throw new ValidationException("login не может содержать пробелы");
            }
            // Проверка уникальности логина
            boolean loginExists = users.values().stream()
                    .anyMatch(user -> newUser.getLogin().equals(user.getLogin()));
            if (loginExists) {
                log.warn("Попытка обновить login на уже используемый: {}", newUser.getLogin());
                throw new DuplicatedDataException("Этот login уже используется");
            }
            oldUser.setLogin(newUser.getLogin());
            log.debug("Login пользователя ID={} обновлён на: {}", oldUser.getId(), newUser.getLogin());
        }
        // Проверка имени
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
            log.debug("Имя пользователя ID={} обновлено на: {}", oldUser.getId(), newUser.getName());
        }
        // Проверка даты рождения
        if (newUser.getBirthday() != null) {
            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                log.warn("Попытка обновить дату рождения на значение в будущем: {}", newUser.getBirthday());
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
            oldUser.setBirthday(newUser.getBirthday());
            log.debug("Дата рождения пользователя ID={} обновлена на: {}", oldUser.getId(), newUser.getBirthday());
        }
        log.debug("Пользователь ID={} успешно обновлён", oldUser.getId());
        return oldUser;
    }

    @GetMapping
    //получаем всех пользователей
    public Collection<User> findAll() {
        return users.values();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
