package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Import({
        UserRepository.class,
        UserRowMapper.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {

    private final UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@mail.ru")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userRepository.save(user);
    }

    @Test
    void shouldSaveUser() {
        assertThat(user.getId()).isPositive();
    }

    @Test
    void shouldFindUserById() {
        Optional<User> foundUser = userRepository.findById(user.getId());

        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(value ->
                        assertThat(value.getEmail()).isEqualTo("test@mail.ru")
                );
    }

    @Test
    void shouldFindUserByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("test@mail.ru");

        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(value ->
                        assertThat(value.getLogin()).isEqualTo("testLogin")
                );
    }

    @Test
    void shouldFindAllUsers() {
        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(1);
    }

    @Test
    void shouldUpdateUser() {
        user.setName("Updated Name");

        User updatedUser = userRepository.update(user);

        assertThat(updatedUser.getName()).isEqualTo("Updated Name");

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser.get().getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldAddFriend() {
        User friend = User.builder()
                .email("friend@mail.ru")
                .login("friendLogin")
                .name("Friend")
                .birthday(LocalDate.of(1999, 5, 5))
                .build();

        userRepository.save(friend);

        userRepository.addFriend(user.getId(), friend.getId());

        List<Long> friends = userRepository.getFriends(user.getId());

        assertThat(friends).contains(friend.getId());
    }

    @Test
    void shouldRemoveFriend() {
        User friend = User.builder()
                .email("friend@mail.ru")
                .login("friendLogin")
                .name("Friend")
                .birthday(LocalDate.of(1999, 5, 5))
                .build();

        userRepository.save(friend);

        userRepository.addFriend(user.getId(), friend.getId());
        userRepository.removeFriend(user.getId(), friend.getId());

        List<Long> friends = userRepository.getFriends(user.getId());

        assertThat(friends).doesNotContain(friend.getId());
    }

    @Test
    void shouldGetCommonFriends() {
        User user2 = User.builder()
                .email("user2@mail.ru")
                .login("user2Login")
                .name("User2")
                .birthday(LocalDate.of(1998, 2, 2))
                .build();

        User commonFriend = User.builder()
                .email("common@mail.ru")
                .login("commonLogin")
                .name("Common Friend")
                .birthday(LocalDate.of(1997, 3, 3))
                .build();

        userRepository.save(user2);
        userRepository.save(commonFriend);

        userRepository.addFriend(user.getId(), commonFriend.getId());
        userRepository.addFriend(user2.getId(), commonFriend.getId());

        List<Long> commonFriends = userRepository.getCommonFriends(user.getId(), user2.getId());

        assertThat(commonFriends).contains(commonFriend.getId());
    }
}
