package com.task.management.api.service;

import com.task.management.api.dto.TaskDto;
import com.task.management.api.dto.UserDto;
import com.task.management.api.enums.TaskStatus;
import com.task.management.api.exceptions.AlreadyFinishedTaskException;
import com.task.management.api.exceptions.AlreadyStartedTaskException;
import com.task.management.api.exceptions.NonStartedTaskException;
import com.task.management.api.mapper.TaskMapper;
import com.task.management.api.mapper.UserMapper;
import com.task.management.api.model.Task;
import com.task.management.api.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {
    @Autowired
    private TaskService service;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private TaskRepository repository;

    @Test
    void testFind() {
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

        when(repository.findById(id)).thenReturn(Optional.of(task));

        var taskDto = service.find(id);

        assertEquals(taskDto.getId(), id);
        assertEquals(taskDto.getTitle(), title);
        assertEquals(taskDto.getDescription(), description);
        assertEquals(taskDto.getDueDate(), now);
    }

    @Test
    void testFindAll() {
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

        when(repository.findAll(Sort.by(Sort.Direction.ASC, "dueDate"))).thenReturn(List.of(task));

        var taskDtoList = service.findAll(Sort.Direction.ASC, "dueDate");

        assertEquals(taskDtoList.get(0).getId(), id);
        assertEquals(taskDtoList.get(0).getTitle(), title);
        assertEquals(taskDtoList.get(0).getDescription(), description);
        assertEquals(taskDtoList.get(0).getDueDate(), now);
    }

    @Test
    void testCreate() {
        Long id = 1L;
        String code = UUID.randomUUID().toString();
        String title = "some title";
        String description = "some description";

        var task = new TaskDto();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);

        when(repository.save(any())).thenReturn(mapper.toEntity(task));

        var savedTask = service.create(task);

        assertEquals(savedTask.getId(), id);
        assertEquals(savedTask.getTitle(), title);
        assertEquals(savedTask.getDescription(), description);
    }

    @Test
    void testUpdate() {
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

        when(repository.save(task)).thenReturn(task);

        var updatedTask = service.update(1L, taskDto);

        assertEquals(updatedTask.getId(), id);
        assertEquals(updatedTask.getTitle(), title);
        assertEquals(updatedTask.getDescription(), description);
        assertEquals(updatedTask.getDueDate(), now);
    }

    @Test
    void testDelete() {
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

        when(repository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testListAllByUser() {
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

        when(repository.findAllByUserAndStatus(any(), any(), any())).thenReturn(List.of(task));

        var foundTasks = service.listAllByUser(new UserDto(), Sort.Direction.ASC, "id", TaskStatus.PENDING);

        assertFalse(foundTasks.isEmpty());
        assertEquals(id, foundTasks.get(0).getId());
        assertEquals(title, foundTasks.get(0).getTitle());
        assertEquals(description, foundTasks.get(0).getDescription());
        assertEquals(now, foundTasks.get(0).getDueDate());
    }

    @Test
    void testBeginTask() {
        Long id = 1L;
        String title = "some title";
        String description = "some description";
        LocalDateTime now = LocalDateTime.now();

        var task = new TaskDto();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(now);
        task.setStatus(TaskStatus.PENDING);

        var startedTask = new TaskDto();
        startedTask.setId(id);
        startedTask.setTitle(title);
        startedTask.setDescription(description);
        startedTask.setDueDate(now);
        startedTask.setStatus(TaskStatus.IN_PROGRESS);

        when(repository.save(any())).thenReturn(mapper.toEntity(startedTask));

        var finalStartedTask = service.beginTask(task);

        assertNotNull(finalStartedTask);
        assertEquals(id, finalStartedTask.getId());
        assertEquals(title, finalStartedTask.getTitle());
        assertEquals(description, finalStartedTask.getDescription());
        assertEquals(now, finalStartedTask.getDueDate());
        assertEquals(TaskStatus.IN_PROGRESS, finalStartedTask.getStatus());
    }

    @Test
    void beginTaskShouldThrowAlreadyStartedTaskException() {
        String errorMessage = "Task already started";

        var task = new TaskDto();
        task.setStatus(TaskStatus.IN_PROGRESS);

        var exception = assertThrows(AlreadyStartedTaskException.class, () -> service.beginTask(task));

        assertEquals(AlreadyStartedTaskException.class, exception.getClass());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void beginTaskShouldThrowAlreadyFinishedTaskException() {
        String errorMessage = "Already finished task";

        var task = new TaskDto();
        task.setStatus(TaskStatus.COMPLETED);

        var exception = assertThrows(AlreadyFinishedTaskException.class, () -> service.beginTask(task));

        assertEquals(AlreadyFinishedTaskException.class, exception.getClass());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testCompleteTask() {
        Long id = 1L;
        String title = "some title";
        String description = "some description";
        LocalDateTime now = LocalDateTime.now();

        var task = new TaskDto();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(now);
        task.setStatus(TaskStatus.IN_PROGRESS);

        var completedTask = new TaskDto();
        completedTask.setId(id);
        completedTask.setTitle(title);
        completedTask.setDescription(description);
        completedTask.setDueDate(now);
        completedTask.setStatus(TaskStatus.COMPLETED);

        when(repository.save(any())).thenReturn(mapper.toEntity(completedTask));

        var finalCompletedTask = service.completeTask(task);

        assertNotNull(finalCompletedTask);
        assertEquals(id, finalCompletedTask.getId());
        assertEquals(title, finalCompletedTask.getTitle());
        assertEquals(description, finalCompletedTask.getDescription());
        assertEquals(now, finalCompletedTask.getDueDate());
        assertEquals(TaskStatus.COMPLETED, finalCompletedTask.getStatus());
    }

    @Test
    void CompleteTaskShouldThrowNonStartedTaskException() {
        String errorMessage = "Non started task";

        var task = new TaskDto();
        task.setStatus(TaskStatus.PENDING);

        var exception = assertThrows(NonStartedTaskException.class, () -> service.completeTask(task));

        assertEquals(NonStartedTaskException.class, exception.getClass());
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void completeTaskShouldThrowAlreadyFinishedTaskException() {
        String errorMessage = "Already finished task";

        var task = new TaskDto();
        task.setStatus(TaskStatus.COMPLETED);

        var exception = assertThrows(AlreadyFinishedTaskException.class, () -> service.completeTask(task));

        assertEquals(AlreadyFinishedTaskException.class, exception.getClass());
        assertEquals(errorMessage, exception.getMessage());
    }


}
