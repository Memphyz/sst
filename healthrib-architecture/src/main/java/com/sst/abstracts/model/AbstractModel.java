package com.sst.abstracts.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public abstract class AbstractModel<ID> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ID id;
	
}
