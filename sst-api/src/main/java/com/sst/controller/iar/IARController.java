package com.sst.controller.iar;

import static com.sst.constants.UrlMappingConstants.V1;
import static com.sst.enums.InvalidEntityMessageType.DIRTY;
import static com.sst.enums.InvalidEntityMessageType.INSUFICIENT;
import static com.sst.enums.InvalidEntityMessageType.MISSING;
import static com.sst.enums.user.permission.UserPermissionType.MANAGER;
import static com.sst.type.level.LevelType.HIGH;
import static com.sst.type.risk.RiskType.CHEMICAL;
import static java.util.stream.Collectors.joining;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sst.abstracts.controller.AbstractController;
import com.sst.api.PurgoMalumClientHttp;
import com.sst.builders.RequestPermissionBuilder;
import com.sst.exceptions.ResourceValidationException;
import com.sst.model.iar.IAR;
import com.sst.resources.iar.IARResource;
import com.sst.service.iar.IARService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(description = "A IAR CRUD", name = "IAR")
@RequestMapping(V1 + "/iar")
public class IARController extends AbstractController<IAR, IARResource, IARService>{
	
	@Autowired
	private PurgoMalumClientHttp purgoMalumHttpClient;
	
	@Override
	public ResponseEntity<?> save(@RequestBody @Valid IAR entity) {
		validateExistingControlMeasures(entity.getExistingControlMeasures());
		validateEntity(entity);
		return super.save(entity);
	}

	@Override
	public ResponseEntity<?> saveAll(@RequestBody @Valid List<IAR> entities) {
		entities.forEach(this::validateEntity);
		String allTexts = entities.stream().map(entity -> entity.getExistingControlMeasures()).collect(joining("__"));
		validateExistingControlMeasures(allTexts);
		return super.saveAll(entities);
	}
	

	private void validateEntity(IAR entity) {
		if(entity.getRiskType().equals(CHEMICAL) && entity.getHazardousAgentCode() == null) {
			throw new ResourceValidationException(MISSING.get("HazardousAgentCode"));
		}
		
		if(entity.getSeverity().equals(HIGH) && entity.getExistingControlMeasures().split(",").length < 3) {
			throw new ResourceValidationException(INSUFICIENT.get("ExistingControlMeasures"));
		}
	}
	
	private void validateExistingControlMeasures(String text) {
		if(purgoMalumHttpClient.hasDirtyWord(text)) {
			throw new ResourceValidationException(DIRTY.get("ExistingControlMeasures"));
		}
	}


	@Override
	protected RequestPermissionBuilder buildPermissionsMapper() {
		return super.buildPermissionsMapper().allowAll().setAllModify(MANAGER);
	}
	

}
