package com.sevenhallo.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel {

    @Column(updatable = false)
    private LocalDateTime createAt;

    @Column(insertable = false)
    private LocalDateTime lastModifiedAt;

    @Column(updatable = false)
    private String createdBy;

    @Column(insertable = false)
    private String lastModifiedBy;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();

        this.createdBy = "system";
        this.lastModifiedBy = "system";
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDateTime.now();

        this.lastModifiedBy = "system";
    }
}
