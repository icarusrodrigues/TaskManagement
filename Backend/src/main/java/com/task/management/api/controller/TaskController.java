package com.task.management.api.controller;

import com.task.management.api.dto.TaskDto;
import com.task.management.api.enums.EnumMessage;
import com.task.management.api.enums.TaskStatus;
import com.task.management.api.enums.UserType;
import com.task.management.api.exceptions.AlreadyFinishedTaskException;
import com.task.management.api.exceptions.AlreadyStartedTaskException;
import com.task.management.api.exceptions.NonStartedTaskException;
import com.task.management.api.response.ResponseHandler;
import com.task.management.api.security.services.UserDetailsImpl;
import com.task.management.api.service.ICrudService;
import com.task.management.api.service.TaskService;
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
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("tasks")
public class TaskController extends CrudController<TaskDto> {
    @Autowired
    private TaskService service;

    @Autowired
    private UserService userService;

    @Autowired
    public TaskController(ICrudService<TaskDto> service) {
        super(service);
    }

    @Operation(summary = "Get a Task by id", description = "Returns the Task with the provided ID.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Return the Task with the ID provided.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": {
                                    "id": 0,
                                    "title": "Some Task",
                                    "description": "Some Description",
                                    "dueDate": "2025-02-19T00:00:00",
                                    "status": "PENDING",
                                    "userId": 0
                                },
                                "message": "Data recovered successfully!",
                                "status": 200
                            }"""))),
            @ApiResponse(responseCode = "404",
                    description = "There is no Task in the database with the provided ID.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Entity not found!",
                                "status": 404
                            }""")))
    })
    @Override
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var foundUser = userService.findByUsername(user.getUsername()).get();

        try {
            var foundTask = service.find(id);

            if (foundTask.getUserId().equals(foundUser.getId()) || foundUser.getUserType().equals(UserType.ADMIN)) {
                return ResponseHandler.generateResponse(super.getById(id), EnumMessage.GET_MESSAGE.message());
            } else {
                return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CANT_ACCESS_ENTITY_MESSAGE.message());
            }

        } catch (NoSuchElementException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message());
        }
    }

    @Operation(summary = "Get all Tasks", description = "Returns a list of all saved Tasks.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Returns a list with all Tasks saved",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": [
                                    {
                                        "id": 0,
                                        "title": "Some Task",
                                        "description": "Some Description",
                                        "dueDate": "2025-02-19T00:00:00",
                                        "status": "PENDING",
                                        "userId": 0
                                    }
                                ],
                                "message": "Data recovered successfully!",
                                "status": 200
                            }"""))),
            @ApiResponse(responseCode = "400",
                    description = "The passed property does not exist in class Task.",
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
                                  @RequestParam(name = "property", defaultValue = "dueDate") String property) {
        try {
            return ResponseHandler.generateResponse(super.list(direction, property), EnumMessage.GET_MESSAGE.message());
        } catch (PropertyReferenceException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.PROPERTY_NOT_FOUND_MESSAGE.message());
        }
    }

    @Operation(summary = "Get all Tasks from the logged in user", description = "Returns a list of all saved Tasks that belong to the logged in user.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Returns a list with all Tasks saved",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": [
                                    {
                                        "id": 0,
                                        "title": "Some Task",
                                        "description": "Some Description",
                                        "dueDate": "2025-02-19T00:00:00",
                                        "status": "PENDING",
                                        "userId": 0
                                    }
                                ],
                                "message": "Data recovered successfully!",
                                "status": 200
                            }""")))
    })
    @GetMapping("/my-tasks")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> listTasksByUser(@RequestParam(name = "direction", defaultValue = "ASC") Sort.Direction direction,
                                             @RequestParam(name = "property", defaultValue = "dueDate") String property,
                                             @RequestParam(name = "status", defaultValue = "PENDING") TaskStatus status) {
        var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var foundUser = userService.findByUsername(user.getUsername()).get();

        try {
            return ResponseHandler.generateResponse(ResponseEntity.ok(service.listAllByUser(foundUser, direction, property, status)), EnumMessage.GET_MESSAGE.message());
        } catch (PropertyReferenceException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.PROPERTY_NOT_FOUND_MESSAGE.message());
        }
    }

    @Operation(summary = "Create Task", description = "Create a new Task in database.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201",
                    description = "Returns task saved.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": {
                                    "id": 0,
                                    "title": "Some Task",
                                    "description": "Some Description",
                                    "dueDate": "2025-02-19T00:00:00",
                                    "status": "PENDING",
                                    "userId": 0
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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> create(@RequestBody @Valid TaskDto dto){
        var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var foundUser = userService.findByUsername(user.getUsername()).get();

        dto.setUserId(foundUser.getId());
        dto.setStatus(TaskStatus.PENDING);

        try {
            return ResponseHandler.generateResponse(super.create(dto), EnumMessage.POST_MESSAGE.message());
        } catch (ConstraintViolationException | DataIntegrityViolationException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CONSTRAINT_VIOLATION_MESSAGE.message());
        }
    }

    @Operation(summary = "Update a Task", description = "Updates a Task that has already been saved.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Returns the updated Task.",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": {
                                    "id": 0,
                                    "title": "Some Task",
                                    "description": "Some Description",
                                    "dueDate": "2025-02-19T00:00:00",
                                    "status": "PENDING",
                                    "userId": 0
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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody TaskDto dto) {
        var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var foundUser = userService.findByUsername(user.getUsername()).get();

        try {
            var foundTask = service.find(id);

            if (foundTask.getUserId().equals(foundUser.getId()) || foundUser.getUserType().equals(UserType.ADMIN)) {
                dto.setId(id);

                if (dto.getTitle() == null)
                    dto.setTitle(foundTask.getTitle());

                if (dto.getDescription() == null)
                    dto.setDescription(foundTask.getDescription());

                if (dto.getStatus() == null)
                    dto.setStatus(foundTask.getStatus());

                if (dto.getDueDate() == null)
                    dto.setDueDate(foundTask.getDueDate());

                if (dto.getUserId() == null || !foundUser.getUserType().equals(UserType.ADMIN))
                    dto.setUserId(foundTask.getUserId());

                return ResponseHandler.generateResponse(ResponseEntity.ok(service.update(id, dto)), EnumMessage.PUT_MESSAGE.message());
            }

            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CANT_ACCESS_ENTITY_MESSAGE.message());

        } catch (NoSuchElementException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message());
        } catch (TransactionSystemException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CONSTRAINT_VIOLATION_MESSAGE.message());
        }
    }

    @Operation(summary = "Delete a Task", description = "Delete the entity with the provided ID.")
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
    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var foundUser = userService.findByUsername(user.getUsername()).get();

        try {
            var foundTask = service.find(id);

            if (foundTask.getUserId().equals(foundUser.getId()) || foundUser.getUserType().equals(UserType.ADMIN))
                return ResponseHandler.generateResponse(super.delete(id), EnumMessage.DELETE_MESSAGE.message());

            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CANT_ACCESS_ENTITY_MESSAGE.message());

        } catch (NoSuchElementException ignored) {
            return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message());
        }
    }

    @Operation(summary = "Start a Task", description = "Start the task with the provided ID.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Task started",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": {
                                    "id": 0,
                                    "title": "Some Task",
                                    "description": "Some Description",
                                    "dueDate": "2025-02-25T22:28:01.04325",
                                    "status": "IN_PROGRESS",
                                    "userId": 0
                                },
                                "message": "Data updated successfully!",
                                "status": 200
                            }"""))),
            @ApiResponse(responseCode = "400",
                    description = "Task already started",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Task already started",
                                "status": 400
                            }"""))),
            @ApiResponse(responseCode = "400",
                    description = "Already finished task",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Already finished task",
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
    @PutMapping("/start/{id}")
    public ResponseEntity<?> startTask(@PathVariable("id") Long id) {
        var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var foundUser = userService.findByUsername(user.getUsername()).get();

        try {
            var foundTask = service.find(id);

            if (foundTask.getUserId().equals(foundUser.getId()) || foundUser.getUserType().equals(UserType.ADMIN))
                return ResponseHandler.generateResponse(ResponseEntity.ok(service.beginTask(foundTask)), EnumMessage.PUT_MESSAGE.message());

            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CANT_ACCESS_ENTITY_MESSAGE.message());

        } catch (AlreadyStartedTaskException | AlreadyFinishedTaskException error) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), error.getMessage());
        } catch (NoSuchElementException error) {
            return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message());
        }
    }

    @Operation(summary = "Complete a Task", description = "Complete the task with the provided ID.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200",
                    description = "Task completed",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": {
                                    "id": 0,
                                    "title": "Some Task",
                                    "description": "Some Description",
                                    "dueDate": "2025-02-19T00:00:00.000000",
                                    "status": "COMPLETED",
                                    "userId": 0
                                },
                                "message": "Data updated successfully!",
                                "status": 200
                            }"""))),
            @ApiResponse(responseCode = "400",
                    description = "Non started task",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Non started task",
                                "status": 400
                            }"""))),
            @ApiResponse(responseCode = "400",
                    description = "Already finished task",
                    content = @Content(examples = @ExampleObject(value = """
                            {
                                "data": null,
                                "message": "Already finished task",
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
    @PutMapping("/complete/{id}")
    public ResponseEntity<?> completeTask(@PathVariable("id") Long id) {
        var user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var foundUser = userService.findByUsername(user.getUsername()).get();

        try {
            var foundTask = service.find(id);

            if (foundTask.getUserId().equals(foundUser.getId()) || foundUser.getUserType().equals(UserType.ADMIN))
                return ResponseHandler.generateResponse(ResponseEntity.ok(service.completeTask(foundTask)), EnumMessage.PUT_MESSAGE.message());

            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), EnumMessage.CANT_ACCESS_ENTITY_MESSAGE.message());

        } catch (NonStartedTaskException | AlreadyFinishedTaskException error) {
            return ResponseHandler.generateResponse(ResponseEntity.badRequest().build(), error.getMessage());
        } catch (NoSuchElementException error) {
        return ResponseHandler.generateResponse(ResponseEntity.notFound().build(), EnumMessage.ENTITY_NOT_FOUND_MESSAGE.message());
    }
    }
}
