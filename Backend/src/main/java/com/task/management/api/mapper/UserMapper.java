package com.task.management.api.mapper;

import com.task.management.api.dto.UserDto;
import com.task.management.api.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends GenericMapper<UserDto, User> {
    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserDto toDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    @Override
    public User toEntity(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }
}
