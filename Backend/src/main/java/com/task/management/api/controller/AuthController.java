package com.task.management.api.controller;

import com.task.management.api.dto.UserDto;
import com.task.management.api.dto.auth.LoginRequestDto;
import com.task.management.api.dto.auth.RegisterRequestDto;
import com.task.management.api.enums.UserType;
import com.task.management.api.response.ResponseHandler;
import com.task.management.api.security.jwt.JwtUtils;
import com.task.management.api.security.services.UserDetailsImpl;
import com.task.management.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginDto) {
        var foundUser = userService.findByUsername(loginDto.getUsername());

        if (foundUser.isEmpty()) {
            foundUser = userService.findByEmail(loginDto.getUsername());
            if (foundUser.isEmpty()) {
                return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "User not found!");
            }
        }

        if (passwordEncoder.matches(loginDto.getPassword(), foundUser.get().getPassword())){
            var userDetails = UserDetailsImpl.build(userService.getUserEntityFindByUsername(foundUser.get().getUsername()));

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            var token = jwtUtils.generateJwtToken(authentication);

            var responseMap = new HashMap<String, String>();
            responseMap.put("username", foundUser.get().getUsername());
            responseMap.put("email", foundUser.get().getEmail());
            responseMap.put("token", token);

            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "User or password doesn't match!");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerDto) {
        var foundUserByUsername = userService.findByUsername(registerDto.getUsername());
        var foundUserByEmail = userService.findByEmail(registerDto.getEmail());

        if (foundUserByUsername.isPresent() && foundUserByEmail.isPresent()) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "Username and Email already in use!");
        }

        if (foundUserByUsername.isPresent()) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "Username already exists!");
        }

        if (foundUserByEmail.isPresent()) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "Email already exists!");
        }

        try {
            UserDto userDto = new UserDto();

            userDto.setUsername(registerDto.getUsername());
            userDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            userDto.setEmail(registerDto.getEmail());
            userDto.setUserType(UserType.USER);

            return ResponseEntity.ok(userService.create(userDto));

        } catch (DataIntegrityViolationException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "User already in use!");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.noContent().build();
    }
}
