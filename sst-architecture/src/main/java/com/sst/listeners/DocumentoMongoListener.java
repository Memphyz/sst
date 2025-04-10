package com.sst.listeners;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import com.sst.abstracts.model.auditable.AbstractModelAuditable;

@Component
public class DocumentoMongoListener extends AbstractMongoEventListener<Object> {

	@SuppressWarnings("rawtypes")
	@Override
	public void onBeforeSave(BeforeSaveEvent<Object> event) {
		if(event.getSource() instanceof AbstractModelAuditable) {
			AbstractModelAuditable entity = (AbstractModelAuditable) event.getSource();
			if(entity.getId() != null && entity.getCreatedBy() == null && entity.getUpdatedBy() != null) {
				entity.setCreatedAt(entity.getUpdatedAt());
				entity.setCreatedBy(entity.getUpdatedBy());
				entity.setUpdatedAt(null);
				entity.setUpdatedBy(null);
			}
		}
		super.onBeforeSave(event);
	}

    
}
