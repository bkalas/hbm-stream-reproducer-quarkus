package com.bkalas.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;



/**
 * @author msomora
 */
public class TraceableListener implements Serializable {

    /**
     * Marks the entity as created.
     *
     * @param entity the traceable persistent entity.
     */
    @PrePersist
    public void prePersist(AbstractTraceableEntity<?> entity) {
        if (!entity.isControlTraceabilityManual()) {
            LocalDateTime date = LocalDateTime.now();
            entity.setCreationDate(date);
            entity.setModificationDate(date);
        }
    }

    /**
     * Marks the entity as changed.
     *
     * @param entity the traceable persistent entity.
     */
    @PreUpdate
    public void preUpdate(AbstractTraceableEntity<?> entity) {
        if (!entity.isControlTraceabilityManual()) {
            entity.setModificationDate(LocalDateTime.now());
        }
    }


}