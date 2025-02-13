package com.task.management.api.dto.auth;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String username;
    private String password;
}
