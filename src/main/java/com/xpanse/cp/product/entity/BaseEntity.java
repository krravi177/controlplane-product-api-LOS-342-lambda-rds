package com.xpanse.cp.product.entity;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

	private LocalDateTime createdDate;
	
	private LocalDateTime updatedDate;
	
	@PrePersist
	public void prePersist() {
		this.setCreatedDate(LocalDateTime.now());
	}
	
	@PreUpdate
	public void preUpdate() {
		this.setUpdatedDate(LocalDateTime.now());
	}
	
}
