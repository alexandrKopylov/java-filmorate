package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage{
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    public UserDbStorage(JdbcTemplate jdbc, RowMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> getAll() {
        return findMany(FIND_ALL_QUERY);
    }



    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {

    }

    @Override
    public void removeFriend(Long userId, Long friendId) {

    }

    @Override
    public List<User> getFriends(Long userId) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        return null;
    }
}
