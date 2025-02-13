package com.task.management.api.repository;

import com.task.management.api.model.Task;
import com.task.management.api.model.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TaskRepository extends IRepository<Task, Long> {

    List<Task> findAllByUser(User user, Sort sort);
}
