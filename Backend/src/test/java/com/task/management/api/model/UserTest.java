package com.task.management.api.model;

import com.task.management.api.enums.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTest {

    @Test
    void testAllArgsConstructor() {
        var username = "Some username";
        var password = "password";
        var email = "some@email.com";
        var listTasks = new ArrayList<Task>();

        var user = new User(username, password, email, listTasks, UserType.USER);

        assertEquals(user.getUsername(), username);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getCreatedTasks(), listTasks);
        assertEquals(user.getUserType(), UserType.USER);
    }

    @Test
    void testEquals() {
        var user1 = new User();
        var user2 = new User();

        assertEquals(user1, user2);
    }
}
