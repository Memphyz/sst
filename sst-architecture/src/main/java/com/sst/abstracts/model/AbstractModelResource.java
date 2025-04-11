package com.sst.abstracts.model;

import com.sst.abstracts.resource.AbstractResource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractModelResource<Model extends AbstractModel<ID>, Resource extends AbstractResource<Model,Resource>, ID> extends AbstractResource<Model, Resource> {
	
	private static final long serialVersionUID = 1L;
	
	private ID id;
	
}
