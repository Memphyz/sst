package com.sst.abstracts.model.auditable;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.sst.abstracts.model.AbstractModel;
import com.sst.abstracts.model.AbstractModelResource;
import com.sst.abstracts.resource.AbstractResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractModelAuditableResource<Model extends AbstractModel<ID>, Resource extends AbstractResource<Model,Resource>, ID> extends AbstractModelResource<Model, Resource, ID> {
	private static final long serialVersionUID = 1L;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	@CreatedBy
	private String createdBy;
	
	@LastModifiedBy
	private String updatedBy;
}

