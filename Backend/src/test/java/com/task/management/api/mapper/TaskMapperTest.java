package com.task.management.api.mapper;

import com.task.management.api.dto.TaskDto;
import com.task.management.api.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskMapperTest {
    @Autowired
    TaskMapper mapper;

    @Test
    void testToDto() {
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

        var taskDto = mapper.toDto(task);

        assertEquals(taskDto.getId(), task.getId());
        assertEquals(taskDto.getTitle(), task.getTitle());
        assertEquals(taskDto.getDescription(), task.getDescription());
        assertEquals(taskDto.getDueDate(), task.getDueDate());
    }

    @Test
    void testToEntity() {
        Long id = 1L;
        String title = "some title";
        String description = "some description";
        LocalDateTime now = LocalDateTime.now();

        var taskDto = new TaskDto();
        taskDto.setId(id);
        taskDto.setTitle(title);
        taskDto.setDescription(description);
        taskDto.setDueDate(now);

        var task = mapper.toEntity(taskDto);

        assertEquals(task.getId(), taskDto.getId());
        assertEquals(task.getTitle(), taskDto.getTitle());
        assertEquals(task.getDescription(), taskDto.getDescription());
        assertEquals(task.getDueDate(), taskDto.getDueDate());
    }
}
