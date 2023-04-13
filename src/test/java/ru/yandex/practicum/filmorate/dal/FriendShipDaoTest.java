package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FriendShipDaoTest {
    private final UserDao userDao;
    private final FriendshipDao friendshipDao;

    @Order(1)
    @Test
    public void addFriendAndRemove_shouldReturnNewFriendId_afterAdd_AndReturnEmptyList_afterRemove() {
        User user = userDao.add(User.builder()
                .name("test")
                .email("test@email.com")
                .login("test1")
                .birthday(LocalDate.of(2000, 1, 17))
                .build());
        User friend = userDao.add(User.builder()
                .name("friend")
                .email("friend@email.com")
                .login("friendTest")
                .birthday(LocalDate.of(1997, 3, 14))
                .build());
        friendshipDao.addFriend(user.getId(), friend.getId());
        assertEquals(userDao.get(user.getId()).get().getFriends(), new HashSet<Integer>(List.of(friend.getId())));
        assertEquals(userDao.get(friend.getId()).get().getFriends(), new HashSet<Integer>());

        friendshipDao.addFriend(friend.getId(), user.getId());
        assertEquals(userDao.get(friend.getId()).get().getFriends(), new HashSet<Integer>(List.of(user.getId())));
        assertEquals(userDao.get(user.getId()).get().getFriends(), new HashSet<Integer>(List.of(friend.getId())));

        friendshipDao.deleteFriendFromUser(user.getId(), friend.getId());
        assertEquals(userDao.get(friend.getId()).get().getFriends(), new HashSet<Integer>(List.of(user.getId())));
        assertEquals(userDao.get(user.getId()).get().getFriends(), new HashSet<Integer>(List.of()));
    }
}
