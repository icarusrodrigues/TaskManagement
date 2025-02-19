package com.task.management.api.service;

import com.task.management.api.dto.UserDto;
import com.task.management.api.mapper.UserMapper;
import com.task.management.api.model.User;
import com.task.management.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
    void shouldUpdate() {
        Long id = 1L;
        String username = "Some username";
        String email = "email@email.com";

        var user = new UserDto();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);

        when(repository.findById(1L)).thenReturn(Optional.of(mapper.toEntity(user)));
        when(repository.save(any())).thenReturn(mapper.toEntity(user));

        var updatedUser = service.update(id, user);

        assertNotNull(updatedUser);
        assertEquals(id, updatedUser.getId());
        assertEquals(username, updatedUser.getUsername());
        assertEquals(email, updatedUser.getEmail());
    }

    @Test
    void shouldFindByUsername() {
        Long id = 1L;
        String username = "Some username";

        var user = new User();
        user.setId(id);
        user.setUsername(username);

        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        var foundUser = service.findByUsername(username);

        assertEquals(foundUser.get().getId(), id);
        assertEquals(foundUser.get().getUsername(), username);
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

    @Test
    void shouldFindByEmail() {
        Long id = 1L;
        String email = "email@email.com";

        var user = new User();
        user.setId(id);
        user.setEmail(email);

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        var foundUser = service.findByEmail(email).get();

        assertEquals(foundUser.getId(), id);
        assertEquals(foundUser.getEmail(), email);
    }
}
