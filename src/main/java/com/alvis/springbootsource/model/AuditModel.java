package com.alvis.springbootsource.model;

import com.alvis.springbootsource.entity.AuditEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditModel {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AuditModel(AuditEntity entity) {
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
    }
}