package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoTest {

    private final UserDao userDao;

    @Order(1)
    @Test
    public void post_shouldReturnUser_afterPostInDb() {

        User user = userDao.add(User.builder()
                .name("test")
                .email("test@email.com")
                .login("test1")
                .birthday(LocalDate.of(2000, 1, 17))
                .build());

        assertEquals(user.getName(), "test");
        assertEquals(user.getEmail(), "test@email.com");
        assertEquals(user.getLogin(), "test1");
    }

    @Order(2)
    @Test
    public void get_shouldReturnUser_afterPostInDb() {
        Optional<User> user = userDao.get(1);
        assertEquals(user.get().getId(), 1);
        assertEquals(user.get().getName(), "test");
        assertEquals(user.get().getLogin(), "test1");
        assertEquals(user.get().getEmail(), "test@email.com");
        assertEquals(user.get().getBirthday(), LocalDate.of(2000, 1, 17));
    }

    @Order(3)
    @Test
    public void post_shouldReturnNewUser_afterPostInDb() {
        User user = userDao.add(User.builder()
                .id(2)
                .name("new User")
                .login("newLogin")
                .email("user@email.com")
                .birthday(LocalDate.of(1990, 2, 16))
                .build());

        assertEquals(user.getName(), "new User");
        assertEquals(user.getEmail(), "user@email.com");
        assertEquals(user.getLogin(), "newLogin");

    }

    @Order(4)
    @Test
    public void get_shouldReturnNewUser_afterPostInDb() {
        Optional<User> user = userDao.get(2);
        assertEquals(user.get().getId(), 2);
        assertEquals(user.get().getName(), "new User");
        assertEquals(user.get().getEmail(), "user@email.com");
        assertEquals(user.get().getLogin(), "newLogin");
        assertEquals(user.get().getBirthday(), LocalDate.of(1990, 2, 16));
    }

    @Order(5)
    @Test
    public void replace_shouldReturnChangedUser_afterReplaceInDb() {
        User user = userDao.update(User.builder()
                .id(1)
                .name("newName")
                .email("newEmail@gmail.com")
                .login("newLogin")
                .birthday(LocalDate.of(1999, 7, 29))
                .build());
        assertEquals(user.getId(), 1);
        assertEquals(user.getName(), "newName");
        assertEquals(user.getLogin(), "newLogin");
        assertEquals(user.getEmail(), "newEmail@gmail.com");
        assertEquals(user.getBirthday(), LocalDate.of(1999, 7, 29));
    }

    @Order(6)
    @Test
    public void getAll_shouldReturnTwoUsers_afterPutInDb() {
        Collection<User> usersFromDb = userDao.getValues();
        Collection<User> correctList = List.of(
                User.builder()
                        .id(1)
                        .name("newName")
                        .email("newEmail@gmail.com")
                        .login("newLogin")
                        .birthday(LocalDate.of(1999, 7, 29))
                        .friends(new HashSet<>())
                        .build(),
                User.builder()
                        .id(2)
                        .name("new User")
                        .login("newLogin")
                        .email("user@email.com")
                        .birthday(LocalDate.of(1990, 2, 16))
                        .friends(new HashSet<>())
                        .build());
        assertEquals(usersFromDb, correctList);
    }

    @Order(7)
    @Test
    public void remove_shouldReturnEmptyOptional_afterRemoveUser() {
        userDao.delete(User.builder()
                .id(1)
                .name("newName")
                .email("newEmail@gmail.com")
                .login("newLogin")
                .birthday(LocalDate.of(1999, 7, 29))
                .friends(new HashSet<>())
                .build());
        Optional<User> uo = userDao.get(1);
        assertEquals(uo, Optional.empty());
    }

    @Order(8)
    @Test
    public void getAll_shouldReturnOneUser_afterRemoveOtherUser() {
        Collection<User> usersFromDb = userDao.getValues();
        Collection<User> correctList = List.of(
                User.builder()
                        .id(2)
                        .name("new User")
                        .login("newLogin")
                        .email("user@email.com")
                        .birthday(LocalDate.of(1990, 2, 16))
                        .friends(new HashSet<>())
                        .build());
        assertEquals(usersFromDb, correctList);
    }

    @Order(9)
    @Test
    public void containsKey_shouldReturnCorrectBooleanValues() {
        boolean contains = userDao.containsKey(2);
        boolean notContains = userDao.containsKey(999);

        assertEquals(contains, true);
        assertEquals(notContains, false);
    }

    @Order(10)
    @Test
    public void containsValue_shouldReturnCorrectBooleanValues() {
        boolean contains = userDao.containsValue(User.builder().name("new User")
                .login("newLogin")
                .email("user@email.com")
                .birthday(LocalDate.of(1990, 2, 16))
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
