package com.sst.controller.iar;

import static com.sst.constants.UrlMappingConstants.V1;
import static com.sst.enums.user.permission.UserPermissionType.MANAGER;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sst.abstracts.controller.AbstractController;
import com.sst.builders.RequestPermissionBuilder;
import com.sst.model.iar.IAR;
import com.sst.resources.iar.IARResource;
import com.sst.service.iar.IARService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(description = "A IAR CRUD", name = "IAR")
@RequestMapping(V1 + "/iar")
public class IARController extends AbstractController<IAR, IARResource, IARService>{
	
	@Override
	protected RequestPermissionBuilder buildPermissionsMapper() {
		return super.buildPermissionsMapper().allowAll().setAllModify(MANAGER);
	}
	

}
