package com.task.management.api.controller;

import com.task.management.api.dto.UserDto;
import com.task.management.api.enums.EnumMessage;
import com.task.management.api.response.ResponseHandler;
import com.task.management.api.service.ICrudService;
import com.task.management.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("users")
public class UserController extends CrudController<UserDto> {
    @Autowired
    private UserService service;

    @Autowired
    public UserController(ICrudService<UserDto> service) {
        super(service);
    }

    @Operation(summary = "Get an User by id", description = "Returns the User with the provided ID.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Return the User with the ID provided.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": {
                                    "id": 0,
                                    "username": "Some User",
                                    "email": "some@email",
                                    "userType": "PERSONAL",
                                    "team": "Some Team",
                                    "tasks": [
                                        {
                                            "id": 0,
                                            "title": "Some Task",
                                            "description": "Some Description",
                                            "dueDate": "2024-03-22T00:00:00"
                                        }
                                    ]
                                },
                                "message": "Data recovered successfully!",
                                "status": 200
                            }"""))),
            @ApiResponse(responseCode = "404",
                    description = "There is no User in the database with the provided ID.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Entity not found!",
                                "status": 404
                            }""")))
    })
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            return ResponseHandler.generateResponse(super.getById(id), EnumMessage.GET_MESSAGE.message());
        } catch (NoSuchElementException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message());
        }
    }

    @Operation(summary = "Get all Users", description = "Returns a list of all saved Users.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Returns a list with all Users saved",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": [
                                    {
                                        "id": 0,
                                        "username": "Some User",
                                        "email": "some@email",
                                        "userType": "PERSONAL",
                                        "team": "Some Team",
                                        "tasks": [
                                            {
                                                "id": 0,
                                                "title": "Some Task",
                                                "description": "Some Description",
                                                "dueDate": "2024-03-22T00:00:00"
                                            }
                                        ]
                                    }
                                ],
                                "message": "Data recovered successfully!",
                                "status": 200
                            }"""))),
            @ApiResponse(responseCode = "400",
                    description = "The passed property does not exist in class User.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Property not found in entity!",
                                "status": 400
                            }"""
                    )))
    })
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> list(@RequestParam(name = "direction", defaultValue = "ASC") Sort.Direction direction,
                                  @RequestParam(name = "property", defaultValue = "id") String property) {
        try {
            return ResponseHandler.generateResponse(super.list(direction, property), EnumMessage.GET_MESSAGE.message());
        } catch (NoSuchElementException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.PROPERTY_NOT_FOUND_MESSAGE.message());
        }
    }

    @Operation(summary = "Create User", description = "Create a new User in database.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Returns user saved.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": {
                                    "id": 0,
                                    "username": "Some User",
                                    "email": "some@email",
                                    "userType": "PERSONAL",
                                    "team": "Some Team",
                                    "tasks": []
                                },
                                "message": "Data saved successfully!",
                                "status": 201
                            }"""))),
            @ApiResponse(responseCode = "400",
                    description = "Entity not valid",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Constraint violated!",
                                "status": 400
                            }""")))
    })
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid UserDto dto){
        try {
            return ResponseHandler.generateResponse(super.create(dto), EnumMessage.POST_MESSAGE.message());
        } catch (ConstraintViolationException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CONSTRAINT_VIOLATION_MESSAGE.message());
        }
    }

    @Operation(summary = "Update an User", description = "Updates an User that has already been saved.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Returns the updated User.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": {
                                    "id": 0,
                                    "username": "Some User",
                                    "email": "some@email",
                                    "userType": "PERSONAL",
                                    "team": "Some Team",
                                    "tasks": []
                                },
                                "message": "Data updated successfully!",
                                "status": 200
                            }"""))),
            @ApiResponse(responseCode = "400",
                    description = "Constraint violated!",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Constraint violated!",
                                "status": 400
                            }"""))),
            @ApiResponse(responseCode = "404",
                    description = "There is no entity in the database with the provided ID.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Entity not found!",
                                "status": 404
                            }""")))
    })
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UserDto dto) {
        try {
            return ResponseHandler.generateResponse(super.update(id, dto), EnumMessage.PUT_MESSAGE.message());
        } catch (NoSuchElementException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message());
        } catch (TransactionSystemException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CONSTRAINT_VIOLATION_MESSAGE.message());
        }
    }

    @Operation(summary = "Delete an User", description = "Delete the entity with the provided ID.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "204",
                    description = "No content",
                    content = @Content(examples = @ExampleObject(value = ""))),
            @ApiResponse(responseCode = "404",
                    description = "There is no entity in the database with the provided ID.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Entity not found!",
                                "status": 404
                            }""")))
    })
    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            return ResponseHandler.generateResponse(super.delete(id), EnumMessage.DELETE_MESSAGE.message());

        } catch (NoSuchElementException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message());

        } catch (DataIntegrityViolationException ignored){
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), "The user is a team leader, change the leader(s) of that team(s) first.");
        }
    }
}
