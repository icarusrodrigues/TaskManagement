package com.task.management.api.repository;

import com.task.management.api.model.User;

import java.util.Optional;

public interface UserRepository extends IRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
