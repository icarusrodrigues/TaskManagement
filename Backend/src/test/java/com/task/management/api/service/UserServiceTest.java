package com.task.management.api.service;

import com.task.management.api.mapper.UserMapper;
import com.task.management.api.model.User;
import com.task.management.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository repository;

    @Autowired
    private UserService service;

    @Autowired
    private UserMapper mapper;

    @Test
    void shouldFindByUsername() {
        Long id = 1L;
        String username = "Some username";

        var user = new User();
        user.setId(id);
        user.setUsername(username);

        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        var foundUser = service.findByUsername(username);

        assertEquals(foundUser.getId(), id);
        assertEquals(foundUser.getUsername(), username);
    }

    @Test
    void shouldGetUserEntityFindByUsername() {
        Long id = 1L;
        String username = "Some username";

        var user = new User();
        user.setId(id);
        user.setUsername(username);

        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        var foundUser = service.getUserEntityFindByUsername(username);

        assertEquals(foundUser.getId(), id);
        assertEquals(foundUser.getUsername(), username);
    }
}
