package com.task.management.api.service;

import com.task.management.api.dto.UserDto;
import com.task.management.api.mapper.GenericMapper;
import com.task.management.api.model.User;
import com.task.management.api.repository.IRepository;
import com.task.management.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CrudService<UserDto, User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserService(GenericMapper<UserDto, User> mapper, IRepository<User, Long> repository) {
        super(mapper, repository);
    }

    public UserDto findByUsername(String username){
        return userRepository.findByUsername(username).map(mapper::toDto).orElseThrow();
    }

    public User getUserEntityFindByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }
}
