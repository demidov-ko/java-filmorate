package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    //В качестве параметра он принимает запрос и идентификатор пользователя.
    protected boolean delete(String query, long id) {
        int rowsDeleted = jdbc.update(query, id);
        return rowsDeleted > 0;
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        //если ни одна строка не будет обновлена в результате выполнения запроса, то возникнет исключение
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
    }

    //добавление записи в базу
    protected long insert(String query, Object... params) {
        //KeyHolder — интерфейс, и в качестве параметра в метод update() необходимо передать его реализацию.
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        //В первом же параметре описываем процесс создания PreparedStatement.
        // Соединение (объект connection) предоставляет JdbcTemplate.
        //Параметры со всеми необходимыми значениями передаются при вызове метода из класса UserRepository
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (int idx = 0; idx < params.length; idx++) {
                Object value = params[idx];

                if (value instanceof java.time.LocalDate localDate) {
                    ps.setDate(idx + 1, java.sql.Date.valueOf(localDate));
                } else {
                    ps.setObject(idx + 1, value);
                }
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }
}
