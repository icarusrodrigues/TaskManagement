package com.task.management.api.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskDtoTest {
    @Test
    void testNoArgsConstructor() {
        Long id = 1L;
        String title = "some title";
        String description = "some description";
        LocalDateTime now = LocalDateTime.now();

        var dto = new TaskDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setDueDate(now);

        assertEquals(dto.getId(), id);
        assertEquals(dto.getTitle(), title);
        assertEquals(dto.getDescription(), description);
        assertEquals(dto.getDueDate(), now);
    }

    @Test
    void testEquals() {
        var dto1 = TaskDto.builder().build();
        var dto2 = TaskDto.builder().build();

        assertEquals(dto1, dto2);
    }

    @Test
    void testEqualsAndHashCode() {
        var dto1 = new TaskDto();
        var dto2 = new TaskDto();

        assertEquals(dto1, dto2);
    }
}
