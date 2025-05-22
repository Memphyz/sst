package com.sst.controller.pcmso;

import static com.sst.constants.UrlMappingConstants.V1;
import static com.sst.enums.InvalidEntityMessageType.MISSING;
import static com.sst.enums.InvalidEntityMessageType.MUST_BE_DIFFERENT;
import static com.sst.enums.InvalidEntityMessageType.MUST_BE_GREATER_THAN;
import static com.sst.enums.user.permission.UserPermissionType.MANAGER;
import static java.lang.Double.parseDouble;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sst.abstracts.controller.AbstractController;
import com.sst.builders.RequestPermissionBuilder;
import com.sst.exceptions.ResourceValidationException;
import com.sst.model.pcmso.PCMSO;
import com.sst.resources.pcmso.PCMSOResource;
import com.sst.service.pcmso.PCMSOService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(description = "A PCMSO CRUD", name = "PCMSO")
@RequestMapping(V1 + "/pcmso")
public class PCMSOController extends AbstractController<PCMSO, PCMSOResource, PCMSOService> {
	
	@Override
	protected RequestPermissionBuilder buildPermissionsMapper() {
		return super.buildPermissionsMapper().allowAll().setAllModify(MANAGER);
	}
	
	@Override
	protected void onUpdate(PCMSOResource entity) {
		validateUpdate(entity);
	}
	
	@Override
	protected void onSave(PCMSOResource entity) {
		validateResource(entity);
	}
	
	@Override
	protected void onUpdate(List<PCMSOResource> entities) {
		entities.forEach(this::validateUpdate);
	}

	@Override
	protected void onSave(List<PCMSOResource> entities) {
		entities.forEach(this::validateResource);
	}
	
	private void validateUpdate(PCMSOResource entity) {
		revisionValidation(entity);
		validateResource(entity);
	}
	
	private void revisionValidation(PCMSOResource entity) {
		if(entity.getReasonRevision() == null || entity.getDocument() == null) {
			throw new ResourceValidationException(entity.getReasonRevision() == null ? MISSING.get("reasonRevision"): MISSING.get("document"));
		}
	}

	private void validateResource(PCMSOResource entity) {
		if(entity.getElaborator().getId().equals(entity.getApprover().getId())) {
			throw new ResourceValidationException(MUST_BE_DIFFERENT.get("elaborator", "approver"));
		}
		if(entity.getPrevieusVersion() != null && parseDouble(entity.getPrevieusVersion()) > parseDouble(entity.getVersion())) {
			throw new ResourceValidationException(MUST_BE_GREATER_THAN.get("previeusVersion", "version"));
		}
	}
}
