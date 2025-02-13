package com.task.management.api.controller;

import com.task.management.api.dto.BaseDto;
import com.task.management.api.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
public abstract class CrudController <T extends BaseDto<Long>> {

    protected ICrudService<T> service;

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.find(id));
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(name = "direction", defaultValue = "ASC") Sort.Direction direction,
                                  @RequestParam(name = "property", defaultValue = "id") String property) {
        return ResponseEntity.ok(service.findAll(direction, property));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody T dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody T dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
