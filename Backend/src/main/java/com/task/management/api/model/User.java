package com.task.management.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.task.management.api.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity<Long>{

    @NotBlank
    private String username;

    @NotBlank
    @JsonIgnore
    private String password;

    @NotBlank
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Task> createdTasks;

    @Enumerated(EnumType.STRING)
    private UserType userType;
}
