package com.task.management.api.dto;

import com.task.management.api.enums.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskDto extends BaseDto<Long>{
    String title;
    String description;
    LocalDateTime dueDate;
    TaskStatus status;
    Long userId;
}
