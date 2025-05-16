package com.sst.controller.psst;

import static com.sst.constants.UrlMappingConstants.V1;
import static com.sst.enums.DisclosureMethodType.EMAIL;
import static com.sst.enums.InvalidEntityMessageType.MISSING;
import static com.sst.enums.InvalidEntityMessageType.MUST_BE_GREATER_THAN;
import static com.sst.enums.user.permission.UserPermissionType.MANAGER;
import static java.lang.Double.parseDouble;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sst.abstracts.controller.AbstractController;
import com.sst.builders.RequestPermissionBuilder;
import com.sst.exceptions.ResourceValidationException;
import com.sst.model.psst.PSST;
import com.sst.resources.psst.PSSTResource;
import com.sst.service.psst.PSSTService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(description = "A PSST CRUD", name = "PSST")
@RequestMapping(V1 + "/psst")
public class PSSTController extends AbstractController<PSST, PSSTResource, PSSTService> {
	
	@Override
	protected RequestPermissionBuilder buildPermissionsMapper() {
		return super.buildPermissionsMapper().allowAll().setAllModify(MANAGER);
	}
	
	@Override
	protected void onUpdate(PSSTResource entity) {
		validateUpdate(entity);
	}
	
	@Override
	protected void onSave(PSSTResource entity) {
		validateResource(entity);
	}
	
	@Override
	protected void onUpdate(List<PSSTResource> entities) {
		entities.forEach(this::validateUpdate);
	}

	@Override
	protected void onSave(List<PSSTResource> entities) {
		entities.forEach(this::validateResource);
	}
	
	private void validateUpdate(PSSTResource entity) {
		revisionValidation(entity);
		validateResource(entity);
	}
	
	private void revisionValidation(PSSTResource entity) {
		if(entity.getReasonRevision() == null || entity.getDocument() == null) {
			throw new ResourceValidationException(entity.getReasonRevision() == null ? MISSING.get("reasonRevision"): MISSING.get("document"));
		}
	}

	private void validateResource(PSSTResource entity) {
		if(entity.getPrevieusVersion() != null && parseDouble(entity.getPrevieusVersion()) > parseDouble(entity.getVersion())) {
			throw new ResourceValidationException(MUST_BE_GREATER_THAN.get("previeusVersion", "version"));
		}
		if(entity.getDisclosureMethod().equals(EMAIL) && (entity.getCcMails() == null || entity.getCcMails().isEmpty())) {
			throw new ResourceValidationException(MISSING.get("ccMails"));
		}
	}

}