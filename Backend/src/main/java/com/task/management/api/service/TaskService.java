package com.task.management.api.service;

import com.task.management.api.dto.TaskDto;
import com.task.management.api.dto.UserDto;
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

    public List<TaskDto> listAllByUser(UserDto user, Sort.Direction direction, String property) {
        return taskRepository.findAllByUser(userMapper.toEntity(user), Sort.by(direction, property))
                .stream().map(mapper::toDto).toList();
    }
}
