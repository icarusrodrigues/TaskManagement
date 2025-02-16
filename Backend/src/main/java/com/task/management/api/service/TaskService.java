package com.task.management.api.service;

import com.task.management.api.dto.TaskDto;
import com.task.management.api.dto.UserDto;
import com.task.management.api.enums.TaskStatus;
import com.task.management.api.exceptions.AlreadyFinishedTaskException;
import com.task.management.api.exceptions.AlreadyStartedTaskException;
import com.task.management.api.exceptions.NonStartedTaskException;
import com.task.management.api.mapper.GenericMapper;
import com.task.management.api.mapper.UserMapper;
import com.task.management.api.model.Task;
import com.task.management.api.repository.IRepository;
import com.task.management.api.repository.TaskRepository;
import com.task.management.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService extends CrudService<TaskDto, Task> {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public TaskService(GenericMapper<TaskDto, Task> mapper, IRepository<Task, Long> repository) {
        super(mapper, repository);
    }

    @Override
    public TaskDto create(TaskDto dto) {
        if (dto.getDueDate() == null)
            dto.setDueDate(LocalDateTime.now().plusWeeks(1));

        return super.create(dto);
    }

    @Override
    public TaskDto update(Long id, TaskDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public List<TaskDto> listAllByUser(UserDto user, Sort.Direction direction, String property, TaskStatus status) {
        return taskRepository.findAllByUserAndStatus(userMapper.toEntity(user), Sort.by(direction, property), status)
                .stream().map(mapper::toDto).toList();
    }

    public TaskDto beginTask(TaskDto task) {
        if (task.getStatus() == TaskStatus.IN_PROGRESS)
            throw new AlreadyStartedTaskException();

        if (task.getStatus() == TaskStatus.COMPLETED)
            throw new AlreadyFinishedTaskException();

        task.setStatus(TaskStatus.IN_PROGRESS);
        return mapper.toDto(repository.save(mapper.toEntity(task)));
    }

    public TaskDto completeTask(TaskDto task) {
        if (task.getStatus() == TaskStatus.PENDING)
            throw new NonStartedTaskException();

        if (task.getStatus() == TaskStatus.COMPLETED)
            throw new AlreadyFinishedTaskException();

        task.setStatus(TaskStatus.COMPLETED);
        return mapper.toDto(repository.save(mapper.toEntity(task)));
    }
}
