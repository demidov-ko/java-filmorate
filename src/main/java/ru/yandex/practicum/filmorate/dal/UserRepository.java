package ru.yandex.practicum.filmorate.dal; //

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String FIND_ALL = "SELECT * FROM users";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";

    private static final String FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String INSERT = "INSERT INTO users(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    private static final String ADD_FRIEND =
            "INSERT INTO friendships(user_id, friend_id) VALUES (?, ?)";

    private static final String REMOVE_FRIEND =
            "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

    private static final String GET_FRIENDS =
            "SELECT friend_id FROM friendships WHERE user_id = ?";

    private static final String COMMON_FRIENDS = """
            SELECT f1.friend_id
            FROM friendships f1
            JOIN friendships f2 ON f1.friend_id = f2.friend_id
            WHERE f1.user_id = ? AND f2.user_id = ?
            """;

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public User save(User user) {
        long id = insert(
                INSERT,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public List<User> findAll() {
        List<User> users = findMany(FIND_ALL);
        users.forEach(this::loadFriends);
        return users;
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL, email);
    }

    public Optional<User> findById(long id) {
        Optional<User> user = findOne(FIND_BY_ID, id);
        user.ifPresent(this::loadFriends);
        return user;
    }

    public void addFriend(long userId, long friendId) {
        jdbc.update(ADD_FRIEND, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        jdbc.update(REMOVE_FRIEND, userId, friendId);
    }

    public List<Long> getFriends(long userId) {
        return jdbc.queryForList(GET_FRIENDS, Long.class, userId);
    }

    public List<Long> getCommonFriends(long userId, long otherId) {
        return jdbc.queryForList(COMMON_FRIENDS, Long.class, userId, otherId);
    }

    private void loadFriends(User user) {
        List<Long> friends = getFriends(user.getId());
        user.setFriendIds(new HashSet<>(friends));
    }
}
