package com.task.management.api.dto.auth;

import lombok.Data;

@Data
public class RegisterRequestDto {
    String username;
    String password;
    String email;
}
