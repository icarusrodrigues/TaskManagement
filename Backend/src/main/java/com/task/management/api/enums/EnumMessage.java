package com.task.management.api.enums;

public enum EnumMessage {
    GET_MESSAGE {
        @Override
        public String message() {
            return "Data recovered successfully!";
        }
    },
    POST_MESSAGE {
        @Override
        public String message() {
            return "Data saved successfully!";
        }
    },
    PUT_MESSAGE {
        @Override
        public String message() {
            return "Data updated successfully!";
        }
    },
    DELETE_MESSAGE {
        @Override
        public String message() {
            return "Data deleted successfully!";
        }
    },
    ENTITY_NOT_FOUND_MESSAGE {
        @Override
        public String message() {
            return "Entity not found!";
        }
    },
    CONSTRAINT_VIOLATION_MESSAGE {
        @Override
        public String message() {
            return "Constraint violated!";
        }
    },
    PROPERTY_NOT_FOUND_MESSAGE {
        @Override
        public String message() {
            return "Property not found in entity!";
        }
    },
    CANT_ACCESS_ENTITY_MESSAGE {
        @Override
        public String message() {
            return "You are not the owner of this entity!";
        }
    },
    DONT_HAVE_PERMISSION_MESSAGE {
        @Override
        public String message() {
            return "You don't have permission to do this!";
        }
    };

    public abstract String message();
}
