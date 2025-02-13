package com.task.management.api.service;


import com.task.management.api.dto.BaseDto;
import com.task.management.api.mapper.GenericMapper;
import com.task.management.api.model.BaseEntity;
import com.task.management.api.repository.IRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@AllArgsConstructor
public abstract class CrudService<T extends BaseDto<Long>, E extends BaseEntity<Long>> implements ICrudService<T> {

    protected GenericMapper<T, E> mapper;
    protected IRepository<E, Long> repository;

    @Override
    public T find(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow();
    }

    @Override
    public List<T> findAll(Sort.Direction direction, String property) {
        return repository.findAll(Sort.by(direction, property)).stream().map(mapper::toDto).toList();
    }

    @Override
    public T create(T dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public T update(Long id, T dto) {
        find(id);
        dto.setId(id);
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void delete(Long id) {
        find(id);
        repository.deleteById(id);
    }
}
