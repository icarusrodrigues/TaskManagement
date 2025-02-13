package com.task.management.api.response;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<?> generateResponse(ResponseEntity<?> response, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", response.getBody());
        map.put("message", message);
        map.put("status", response.getStatusCode().value());

        return new ResponseEntity<>(map, response.getStatusCode());
    }
}
