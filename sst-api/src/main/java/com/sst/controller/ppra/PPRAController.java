package com.sst.controller.ppra;

import static com.sst.constants.UrlMappingConstants.V1;
import static com.sst.enums.user.permission.UserPermissionType.MANAGER;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sst.abstracts.controller.AbstractController;
import com.sst.builders.RequestPermissionBuilder;
import com.sst.model.ppra.PPRA;
import com.sst.resources.ppra.PPRAResource;
import com.sst.service.ppra.PPRAService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(description = "A PPRA CRUD", name = "PPRA")
@RequestMapping(V1 + "/ppra")
public class PPRAController extends AbstractController<PPRA, PPRAResource, PPRAService> {
	
	@Override
	protected RequestPermissionBuilder buildPermissionsMapper() {
		return super.buildPermissionsMapper().allowAll().setAllModify(MANAGER);
	}
	
	

}
