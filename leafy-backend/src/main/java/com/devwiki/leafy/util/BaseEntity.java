package com.devwiki.leafy.util;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate // @CreatedBy 대신 @CreatedDate 사용
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // @LastModifiedBy 대신 @LastModifiedDate 사용
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
