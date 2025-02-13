package com.task.management.api.dto.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RegisterRequestDtoTest {

    @Test
    void testDataAnnotation() {
        String username = "Some username";
        String password = "Some password";
        String email = "some@email.com";

        var register = new RegisterRequestDto();
        register.setUsername(username);
        register.setPassword(password);
        register.setEmail(email);

        assertEquals(register.getUsername(), username);
        assertEquals(register.getPassword(), password);
        assertEquals(register.getEmail(), email);
    }
}
