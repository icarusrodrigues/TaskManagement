package com.task.management.api.model;

import com.task.management.api.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TaskTest {

    @Test
    void testNoArgsConstructor() {
        Long id = 1L;
        String code = UUID.randomUUID().toString();
        String title = "some title";
        String description = "some description";
        LocalDateTime now = LocalDateTime.now();

        var task = new Task();
        task.setId(id);
        task.setCode(code);
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(now);

        assertEquals(task.getId(), id);
        assertEquals(task.getCode(), code);
        assertEquals(task.getTitle(), "some title");
        assertEquals(task.getTitle(), "some title");
        assertEquals(task.getDescription(), "some description");
        assertEquals(task.getDueDate(), now);
    }
    @Test
    void testAllArgsConstructor() {
        var user = new User();

        LocalDateTime now = LocalDateTime.now();
        var task = new Task("some title", "some description", TaskStatus.PENDING, now, user);

        assertEquals(task.getTitle(), "some title");
        assertEquals(task.getDescription(), "some description");
        assertEquals(task.getDueDate(), now);
    }

    @Test
    void testEquals() {
        var task1 = new Task();
        var task2 = new Task();

        assertEquals(task1, task2);
    }

    @Test
    void testPrePersist() {
        var task1 = new Task();
        task1.prePersist();

        assertNotNull(task1.getCode());
    }

    @Test
    void testPreUpdate() {
        var task1 = new Task();
        task1.preUpdate();

        assertNotNull(task1.getCode());
    }
}
