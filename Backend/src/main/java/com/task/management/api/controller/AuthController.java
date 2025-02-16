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
        try {
            var foundUser = userService.findByUsername(loginDto.getUsername());

            if (passwordEncoder.matches(loginDto.getPassword(), foundUser.getPassword())){
                var userDetails = UserDetailsImpl.build(userService.getUserEntityFindByUsername(foundUser.getUsername()));

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                var token = jwtUtils.generateJwtToken(authentication);

                var responseMap = new HashMap<String, String>();
                responseMap.put("username", foundUser.getUsername());
                responseMap.put("email", foundUser.getEmail());
                responseMap.put("token", token);

                return ResponseEntity.ok(responseMap);
            } else {
                return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "User or password doesn't match!");
            }

        } catch (NoSuchElementException exception) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "User or password doesn't match!");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerDto) {
        try {
            UserDto userDto = new UserDto();

            userDto.setUsername(registerDto.getUsername());
            userDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            userDto.setEmail(registerDto.getEmail());
            userDto.setUserType(UserType.USER);

            return ResponseEntity.ok(userService.create(userDto));

        } catch (DataIntegrityViolationException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "Username already in use!");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.noContent().build();
    }
}
