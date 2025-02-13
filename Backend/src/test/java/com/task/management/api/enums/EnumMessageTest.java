package com.task.management.api.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EnumMessageTest {
    @Test
    void testValues() {
        List<EnumMessage> messages = List.of(
                EnumMessage.GET_MESSAGE,
                EnumMessage.POST_MESSAGE,
                EnumMessage.PUT_MESSAGE,
                EnumMessage.DELETE_MESSAGE,
                EnumMessage.PROPERTY_NOT_FOUND_MESSAGE,
                EnumMessage.CONSTRAINT_VIOLATION_MESSAGE,
                EnumMessage.PROPERTY_NOT_FOUND_MESSAGE,
                EnumMessage.CANT_ACCESS_ENTITY_MESSAGE,
                EnumMessage.DONT_HAVE_PERMISSION_MESSAGE);

        assertTrue(Arrays.stream(EnumMessage.values()).toList().containsAll(messages));
    }

    @Test
    void testMessage() {
        assertEquals(EnumMessage.GET_MESSAGE.message(), "Data recovered successfully!");
        assertEquals(EnumMessage.POST_MESSAGE.message(), "Data saved successfully!");
        assertEquals(EnumMessage.PUT_MESSAGE.message(), "Data updated successfully!");
        assertEquals(EnumMessage.DELETE_MESSAGE.message(), "Data deleted successfully!");
        assertEquals(EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message(), "Entity not found!");
        assertEquals(EnumMessage.CONSTRAINT_VIOLATION_MESSAGE.message(), "Constraint violated!");
        assertEquals(EnumMessage.PROPERTY_NOT_FOUND_MESSAGE.message(), "Property not found in entity!");
        assertEquals(EnumMessage.CANT_ACCESS_ENTITY_MESSAGE.message(), "You are not the owner of this entity!");
        assertEquals(EnumMessage.DONT_HAVE_PERMISSION_MESSAGE.message(), "You don't have permission to do this!");
    }
}
