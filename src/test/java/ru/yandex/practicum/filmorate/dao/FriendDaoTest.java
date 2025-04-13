package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDao.class, FriendDao.class})
class FriendDaoTest {

    private final UserDao userDao;
    private final FriendDao friendDao;

    @Test
    void testAddFriendAndGetFriends() {
        User user1 = User.builder()
                .email("a@a.com")
                .login("userA")
                .name("User A")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        Long id1 = userDao.create(user1).getId();

        User user2 = User.builder()
                .email("b@b.com")
                .login("userB")
                .name("User B")
                .birthday(LocalDate.of(1991, 2, 2))
                .build();
        Long id2 = userDao.create(user2).getId();

        friendDao.addFriend(id1, id2);

        List<User> friends = friendDao.getFriends(id1);

        assertThat(friends).extracting(User::getId).contains(id2);
    }
}
