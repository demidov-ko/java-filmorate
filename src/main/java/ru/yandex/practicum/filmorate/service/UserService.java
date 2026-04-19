package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(NewUserRequest request) {
        User user = UserMapper.mapToUser(request);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    public UserDto updateUser(UpdateUserRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        UserMapper.updateUserFields(user, request);
        return UserMapper.mapToUserDto(userRepository.update(user));
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto getUserById(long id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void addFriend(long userId, long friendId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Друг не найден"));

        userRepository.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Друг не найден"));

        userRepository.removeFriend(userId, friendId);
    }

    public List<UserDto> getFriends(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return userRepository.getFriends(userId).stream()
                .map(this::getUserById)
                .toList();
    }

    public List<UserDto> getCommonFriends(long userId, long otherId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userRepository.findById(otherId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return userRepository.getCommonFriends(userId, otherId).stream()
                .map(this::getUserById)
                .toList();
    }
}
