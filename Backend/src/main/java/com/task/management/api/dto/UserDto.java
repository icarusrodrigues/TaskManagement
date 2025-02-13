package com.task.management.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.task.management.api.enums.UserType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true)
public class UserDto extends BaseDto<Long> {

    String username;

    @JsonIgnore
    String password;

    String email;

    List<TaskDto> createdTasks = new ArrayList<>();

    UserType userType;
}
