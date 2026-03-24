package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    //создаёт нового пользователя
    @Override
    public User create(User user) {
        log.info("Создание нового пользователя: {}", user.getLogin());
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Пользователь {} успешно создан с ID: {}", user.getLogin(), user.getId());
        return user;
    }

    //обновляет существующего пользователя
    @Override
    public User update(User newUser) {
        log.info("Полная замена пользователя ID={}: {}", newUser.getId(), newUser.getLogin());

        if (newUser.getId() == null) {
            log.warn("Попытка обновить пользователя без id");
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            log.warn("Попытка найти пользователя с несуществующим id: {}", newUser.getId());
            throw new NotFoundException("Пользователь с id " + newUser.getId() + " не найден");
        }

        validateUser(newUser);
        users.put(newUser.getId(), newUser);
        log.debug("Пользователь ID={} успешно заменён на новую версию", newUser.getId());
        return newUser;
    }

    //получает всех пользователей из хранилища
    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    //возвращает пользователя по id
    @Override
    public User findById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    //валидация фильма по всем правилам (при создании и полной замене)
    private void validateUser(final User user) {
        // Проверка email: не null, не пустой, содержит '@'
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Попытка создать пользователя с не указанным email");
            throw new ValidationException("email не указан");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Попытка создать пользователя с некорректным email: {}", user.getEmail());
            throw new ValidationException("email должен содержать символ '@'");
        }

        //проверяем уникальность email
        for (User u : users.values()) {
            if (u.getId().equals(user.getId())) {
                continue;
            }
            if (u.getEmail().equals(user.getEmail())) {
                log.warn("Попытка создать пользователя с уже существующим email: {}", user.getEmail());
                throw new DuplicatedDataException("Этот email уже используется");
            }
        }

        // проверка логина на null, пустоту и пробелы
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Попытка создать пользователя с не указанным login");
            throw new ValidationException("login должен быть указан");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Попытка создать пользователя с login, содержащим пробелы: {}", user.getLogin());
            throw new ValidationException("login не может содержать пробелы");
        }

        //проверяем уникальность логина
        for (User u : users.values()) {
            if (u.getId().equals(user.getId())) {
                continue;
            }
            if (u.getLogin().equals(user.getLogin())) {
                log.warn("Попытка создать пользователя с уже существующим login: {}", user.getLogin());
                throw new DuplicatedDataException("Этот login уже используется");
            }
        }

        // имя может быть пустым — тогда используем логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя не указано, установлено значение: {}", user.getName());
        }

        // проверка даты рождения: не null и не в будущем
        if (user.getBirthday() == null) {
            log.warn("Попытка создать пользователя без указания даты рождения");
            throw new ValidationException("Дата рождения должна быть указана");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Попытка создать пользователя с датой рождения в будущем: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
