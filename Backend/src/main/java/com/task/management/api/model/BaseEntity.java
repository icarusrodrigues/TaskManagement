package com.task.management.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity<T extends Number> implements Serializable, IPrePersist, IPreUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

    protected String code;

    @PrePersist
    public void prePersist() {
        if (code == null){
            code = UUID.randomUUID().toString();
        }

        prePersistAction();
    }

    @PreUpdate
    public void preUpdate() {
        if (code == null){
            code = UUID.randomUUID().toString();
        }

        preUpdateAction();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity<?> entity = (BaseEntity<?>) o;
        return Objects.equals(id, entity.getId());
    }
}
