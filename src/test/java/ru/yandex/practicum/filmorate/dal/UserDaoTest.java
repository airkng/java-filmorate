package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class UserDaoTest {

    private final UserDao userDao;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void addUsersToDb() {
        userDao.add(User.builder()
                .name("test")
                .email("test@email.com")
                .login("test1")
                .birthday(LocalDate.of(2000, 1, 17))
                .build());

        userDao.add(User.builder()
                .name("test2")
                .email("test2@email.com")
                .login("test2")
                .birthday(LocalDate.of(2002, 2, 18))
                .build());
    }

    @AfterEach
    public void deleteUsersFromDb() {
        userDao.delete(User.builder().id(1).build());
        userDao.delete(User.builder().id(2).build());
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
    }

    @Test
    public void post_shouldReturnUser_afterPostInDb() {
        User user = userDao.add(User.builder()
                .name("name")
                .email("test@email.com")
                .login("lloggin")
                .birthday(LocalDate.of(1980, 4, 17))
                .friends(new HashSet<>())
                .build());

        assertEquals(user.getName(), "name");
        assertEquals(user.getEmail(), "test@email.com");
        assertEquals(user.getLogin(), "lloggin");

        assertEquals(userDao.get(3), Optional.of(user));
        userDao.delete(user);
    }

    @Test
    public void get_shouldReturnUser_afterPostInDb() {
        Optional<User> user = userDao.get(1);
        assertEquals(user.get().getId(), 1);
        assertEquals(user.get().getName(), "test");
        assertEquals(user.get().getLogin(), "test1");
        assertEquals(user.get().getEmail(), "test@email.com");
        assertEquals(user.get().getBirthday(), LocalDate.of(2000, 1, 17));
    }

    @Test
    public void post_shouldReturnNewUser_afterPostInDb() {
        User user = userDao.add(User.builder()
                .name("new User")
                .login("newLogin")
                .email("user@email.com")
                .birthday(LocalDate.of(1990, 2, 16))
                .friends(new HashSet<>())
                .build());

        assertEquals(user.getName(), "new User");
        assertEquals(user.getEmail(), "user@email.com");
        assertEquals(user.getLogin(), "newLogin");
        userDao.delete(user);

    }


    @Test
    public void get_shouldReturnNewUser_afterPostInDb() {
        Optional<User> user = userDao.get(2);
        assertEquals(user.get().getId(), 2);
        assertEquals(user.get().getName(), "test2");
        assertEquals(user.get().getEmail(), "test2@email.com");
        assertEquals(user.get().getLogin(), "test2");
        assertEquals(user.get().getBirthday(), LocalDate.of(2002, 2, 18));
    }


    @Test
    public void replace_shouldReturnChangedUser_afterReplaceInDb() {
        User user = userDao.update(User.builder()
                .id(1)
                .name("newName")
                .email("newEmail@gmail.com")
                .login("newLogin")
                .birthday(LocalDate.of(1999, 7, 29))
                .friends(new HashSet<>())
                .build());

        assertEquals(user.getId(), 1);
        assertEquals(user.getName(), "newName");
        assertEquals(user.getLogin(), "newLogin");
        assertEquals(user.getEmail(), "newEmail@gmail.com");
        assertEquals(user.getBirthday(), LocalDate.of(1999, 7, 29));
        assertEquals(userDao.get(1), Optional.of(user));
    }

    @Test
    public void getAll_shouldReturnTwoUsers_afterPutInDb() {
        Collection<User> usersFromDb = userDao.getValues();
        Collection<User> correctList = List.of(
                User.builder()
                        .name("test")
                        .email("test@email.com")
                        .login("test1")
                        .birthday(LocalDate.of(2000, 1, 17))
                        .friends(new HashSet<>())
                        .build(),
                User.builder()
                        .id(2)
                        .name("test2")
                        .login("test2")
                        .email("test2@email.com")
                        .birthday(LocalDate.of(2002, 2, 18))
                        .friends(new HashSet<>())
                        .build());
        assertEquals(usersFromDb, correctList);
    }

    @Test
    public void remove_shouldReturnEmptyOptional_afterRemoveUser() {
        userDao.delete(User.builder()
                .id(1)
                .build());
        Optional<User> uo = userDao.get(1);
        assertEquals(uo, Optional.empty());
    }

    @Test
    public void getAll_shouldReturnOneUser_afterRemoveOtherUser() {
        userDao.delete(User.builder().id(1).build());
        Collection<User> usersFromDb = userDao.getValues();
        Collection<User> correctList = List.of(
                User.builder()
                        .id(2)
                        .name("test2")
                        .login("test2")
                        .email("test2@email.com")
                        .birthday(LocalDate.of(2002, 2, 18))
                        .friends(new HashSet<>())
                        .build());
        assertEquals(usersFromDb, correctList);
    }

    @Test
    public void containsKey_shouldReturnCorrectBooleanValues() {
        boolean contains = userDao.containsKey(2);
        boolean notContains = userDao.containsKey(999);

        assertEquals(contains, true);
        assertEquals(notContains, false);
    }

    @Test
    public void containsValue_shouldReturnCorrectBooleanValues() {
        boolean contains = userDao.containsValue(User.builder()
                .id(2)
                .name("test2")
                .login("test2")
                .email("test2@email.com")
                .birthday(LocalDate.of(2002, 2, 18))
                .friends(new HashSet<>())
                .build());
        boolean notContains = userDao.containsValue(User.builder()
                .name("TEST")
                .login("newLogin")
                .email("user@email.com")
                .birthday(LocalDate.of(1990, 2, 16))
                .friends(new HashSet<>())
                .build());
        assertEquals(contains, true);
        assertEquals(notContains, false);
    }
}
