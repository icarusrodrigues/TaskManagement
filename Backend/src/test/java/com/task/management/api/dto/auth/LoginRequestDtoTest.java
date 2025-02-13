package com.task.management.api.dto.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LoginRequestDtoTest {

    @Test
    void testDataAnnotation() {
        String username = "Some username";
        String password = "Some password";

        var login = new LoginRequestDto();
        login.setUsername(username);
        login.setPassword(password);

        assertEquals(login.getUsername(), username);
        assertEquals(login.getPassword(), password);
    }
}
