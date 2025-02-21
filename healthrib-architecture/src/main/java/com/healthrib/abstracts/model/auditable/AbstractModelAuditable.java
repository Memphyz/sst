package com.healthrib.abstracts.model.auditable;
import java.time.LocalDateTime;

import com.healthrib.abstracts.model.AbstractModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractModelAuditable<ID> extends AbstractModel<ID> {
	private static final long serialVersionUID = 1L;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
}
