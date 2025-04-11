package com.sst.abstracts.resource;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.springframework.beans.BeanUtils;

import com.sst.abstracts.model.AbstractModel;

public abstract class AbstractResource<M extends AbstractModel<?>, R extends AbstractResource<?, ?>> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
     * Convert the Resource to entity
     */
    public M toModel() {
        try {
            Class<M> modelClass = (Class<M>) getModelClass();
            M model = modelClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(this, model);
            afterToModel(model);
            return model;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao converter Resource para Model", e);
        }
    }
    
    @SuppressWarnings("unchecked")
	private Class<M> getModelClass() {
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType type = (ParameterizedType) superclass;
        return (Class<M>) Arrays.asList(type.getActualTypeArguments()).get(0);
    }
    
    /**
     * Update existing model by resource data
     */
    public void updateModel(M model) {
        BeanUtils.copyProperties(this, model, getIgnoredPropertiesForUpdate());
        afterUpdateModel(model);
    }
    
    /**
     * Copy model params to resource
     * */
    @SuppressWarnings("unchecked")
	public R copy(M model) {
    	BeanUtils.copyProperties(model, this);
    	return (R) this;
    }

    /**
     * Hook method for customization after update model
     */
    protected void afterUpdateModel(M model) {
        // Ready to be override
    }

    /**
     * Hook method for customization after conversion from Resource to Entity
     */
    protected void afterToModel(M model) {
        // Ready to be override
    }
    
    protected String[] getIgnoredPropertiesForUpdate() {
        return new String[]{"id", "createdAt", "updatedAt", "createdBy", "updatedBy"};
    }
}
