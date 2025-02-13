package com.task.management.api.response;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ResponseHandleTest {

    @Test
    void testGenerateResponseHandler() {
        var response = ResponseHandler.generateResponse(ResponseEntity.ok().build(), "OK");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
