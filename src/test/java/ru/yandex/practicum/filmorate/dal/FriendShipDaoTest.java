package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.dal.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class FriendShipDaoTest {
    private final UserDao userDao;
    private final FriendshipDao friendshipDao;
    private final JdbcTemplate jdbcTemplate;

    private User user = User.builder()
            .id(1)
            .name("test")
            .email("test@email.com")
            .login("test1")
            .birthday(LocalDate.of(2000, 1, 17))
            .build();
    private User friend = User.builder()
            .id(2)
            .name("friend")
            .email("friend@email.com")
            .login("friendTest")
            .birthday(LocalDate.of(1997, 3, 14))
            .build();
    private User friend2 = User.builder()
            .id(3)
            .name("friend2")
            .email("friend2@email.com")
            .login("friend2Test")
            .birthday(LocalDate.of(1996, 6, 16))
            .build();

    @BeforeEach
    public void addUsersToDb() {
        userDao.add(user);
        userDao.add(friend);
        userDao.add(friend2);
    }

    @AfterEach
    public void removeUsersFromDb() {
        friendshipDao.deleteAllFriendsFromUser(1);
        friendshipDao.deleteAllFriendsFromUser(2);
        friendshipDao.deleteAllFriendsFromUser(3);
        userDao.delete(user);
        userDao.delete(friend);
        userDao.delete(friend2);
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
    }

    @Test
    public void addFriend_shouldReturnNewFriendId_afterAdd() {
        friendshipDao.addFriend(user.getId(), friend.getId());
        assertEquals(userDao.get(user.getId()).get().getFriends(), new HashSet<Integer>(List.of(friend.getId())));
        assertEquals(userDao.get(friend.getId()).get().getFriends(), new HashSet<Integer>());

        friendshipDao.addFriend(friend.getId(), user.getId());
        assertEquals(userDao.get(friend.getId()).get().getFriends(), new HashSet<Integer>(List.of(user.getId())));
        assertEquals(userDao.get(user.getId()).get().getFriends(), new HashSet<Integer>(List.of(friend.getId())));
    }

    @Test
    public void removeFriend_shouldReturnEmptyFriendList() {
        friendshipDao.addFriend(user.getId(), friend.getId());
        friendshipDao.addFriend(friend.getId(), user.getId());
        friendshipDao.deleteFriendFromUser(user.getId(), friend.getId());

        assertEquals(userDao.get(friend.getId()).get().getFriends(), new HashSet<Integer>(List.of(user.getId())));
        assertEquals(userDao.get(user.getId()).get().getFriends(), new HashSet<Integer>(List.of()));
    }

    @Test
    public void addFriend_shouldReturnNewFriendId() {
        friendshipDao.addFriend(friend.getId(), friend2.getId());
        friendshipDao.addFriend(friend.getId(), user.getId());

        assertEquals(userDao.get(friend.getId()).get().getFriends(), new HashSet<Integer>(List.of(user.getId(), friend2.getId())));
    }

    @Test
    public void getFriends_shouldReturnCorrectSet_afterRemove() {
        friendshipDao.addFriend(user.getId(), friend.getId());
        friendshipDao.addFriend(user.getId(), friend2.getId());
        assertEquals(userDao.get(user.getId()).get().getFriends(), new HashSet<Integer>(List.of(friend.getId(), friend2.getId())));

        friendshipDao.deleteFriendFromUser(user.getId(), friend.getId());
        assertEquals(userDao.get(user.getId()).get().getFriends(), new HashSet<Integer>(List.of(friend2.getId())));
    }
}
