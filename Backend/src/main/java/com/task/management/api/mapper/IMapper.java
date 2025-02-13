package com.task.management.api.mapper;

public interface IMapper <T, E>{
    T toDto(E entity);
    E toEntity(T dto);
}
