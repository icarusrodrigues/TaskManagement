package com.task.management.api.controller;


import com.task.management.api.dto.UserDto;
import com.task.management.api.dto.auth.LoginRequestDto;
import com.task.management.api.dto.auth.RegisterRequestDto;
import com.task.management.api.enums.UserType;
import com.task.management.api.mapper.UserMapper;
import com.task.management.api.security.jwt.JwtUtils;
import com.task.management.api.service.UserService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @LocalServerPort
    private int port;

    @Test
    void loginShouldReturnOkResponse() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        String username = "Some username";
        String password = "Some password";
        String email = "some@email";

        var login = new LoginRequestDto();
        login.setUsername(username);
        login.setPassword(password);

        var savedUser = new UserDto();
        savedUser.setUsername(username);
        savedUser.setPassword(passwordEncoder.encode(password));
        savedUser.setEmail(email);
        savedUser.setUserType(UserType.ADMIN);

        when(userService.findByUsername(login.getUsername())).thenReturn(Optional.of(savedUser));
        when(userService.getUserEntityFindByUsername(login.getUsername())).thenReturn(userMapper.toEntity(savedUser));

        given().log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(login)
                .post("auth/login")
                .then().log().all()
                .statusCode(200)
                .body("email", equalTo(savedUser.getEmail()))
                .body("username", equalTo(savedUser.getUsername()));
    }

    @Test
    void loginShouldReturnBadRequestWhenPasswordDontMatch() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        String username = "Some username";
        String password = "Some password";

        var login = new LoginRequestDto();
        login.setUsername(username);
        login.setPassword(password + 1);

        var savedUser = new UserDto();
        savedUser.setUsername(username);
        savedUser.setPassword(passwordEncoder.encode(password));

        when(userService.findByUsername(login.getUsername())).thenReturn(Optional.of(savedUser));

        given().log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(login)
                .post("auth/login")
                .then().log().all()
                .statusCode(400)
                .body("data", equalTo(null))
                .body("message", equalTo("User or password doesn't match!"))
                .body("status", equalTo(400));
    }

    @Test
    void loginShouldReturnBadRequestWhenUserDontExist() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        String username = "Some username";
        String password = "Some password";

        var login = new LoginRequestDto();
        login.setUsername(username);
        login.setPassword(password);

        when(userService.findByUsername(login.getUsername())).thenReturn(Optional.empty());
        when(userService.findByEmail(login.getUsername())).thenReturn(Optional.empty());

        given().log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(login)
                .post("auth/login")
                .then().log().all()
                .statusCode(400)
                .body("data", equalTo(null))
                .body("message", equalTo("User not found!"))
                .body("status", equalTo(400));;
    }

    @Test
    void signUpShouldReturnOk() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        String username = "Some username";
        String password = "Some password";
        String email = "some@email";

        var register = new RegisterRequestDto();
        register.setUsername(username);
        register.setPassword(password);
        register.setEmail(email);

        var createdUser = new UserDto();
        createdUser.setUsername(username);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setEmail(email);

        when(userService.create(createdUser)).thenReturn(createdUser);

        given().log().all()
                .when()
                .contentType(ContentType.JSON)
                .body(register)
                .post("auth/sign-up")
                .then().log().all()
                .statusCode(200 );
    }

    @Test
    void logoutShouldReturnNoContent() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;

        given().log().all()
                .when()
                .contentType(ContentType.JSON)
                .post("auth/logout")
                .then().log().all()
                .statusCode(204);
    }
}
