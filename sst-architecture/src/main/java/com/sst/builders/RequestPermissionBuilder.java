package com.sst.builders;

import static com.sst.enums.user.permission.UserPermissionType.ALL;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sst.enums.user.permission.UserPermissionType;

/**
 * A permission builder class that maps a permission for each method on default CRUD
 * 
 * 
 * @author Lucas Ribeiro
 * */
public class RequestPermissionBuilder {

	private Map<String, List<UserPermissionType>> mapper = new HashMap<String, List<UserPermissionType>>();
	
	/*
	 * Creates a instance of RequestPermissionBuilder
	 * 
	 * */
	public static RequestPermissionBuilder create() {
		return new RequestPermissionBuilder();
	}

	public RequestPermissionBuilder allowAll() {
		return setAll(ALL.toArray(new UserPermissionType[0]));
	}

	public RequestPermissionBuilder setAll(UserPermissionType... permissions) {
		return this.getByIdPermissions(permissions).deleteByIdPermissions(permissions).deletePermissions(permissions)
				.getAllByPermissions(permissions).saveAllPermissions(permissions).updatePermissions(permissions)
				.savePermissions(permissions).deleteAllPermissions(permissions);
	}

	public RequestPermissionBuilder setAllModify(UserPermissionType... permissions) {
		return deleteByIdPermissions(permissions).deletePermissions(permissions).saveAllPermissions(permissions)
				.updatePermissions(permissions).savePermissions(permissions).deleteAllPermissions(permissions);
	}

	public RequestPermissionBuilder setAllGetter(UserPermissionType... permissions) {
		return this.getByIdPermissions(permissions).getAllByPermissions(permissions);
	}

	public RequestPermissionBuilder getByIdPermissions(UserPermissionType... permissions) {
		mapper.put("getById", asList(permissions));
		return this;
	}

	public RequestPermissionBuilder getAllByPermissions(UserPermissionType... permissions) {
		mapper.put("getAllBy", asList(permissions));
		return this;
	}

	public RequestPermissionBuilder savePermissions(UserPermissionType... permissions) {
		mapper.put("save", asList(permissions));
		return this;
	}

	public RequestPermissionBuilder saveAllPermissions(UserPermissionType... permissions) {
		mapper.put("saveAll", asList(permissions));
		return this;
	}

	public RequestPermissionBuilder deleteAllPermissions(UserPermissionType... permissions) {
		mapper.put("deleteAll", asList(permissions));
		return this;
	}

	public RequestPermissionBuilder updatePermissions(UserPermissionType... permissions) {
		mapper.put("update", asList(permissions));
		return this;
	}

	public RequestPermissionBuilder deletePermissions(UserPermissionType... permissions) {
		mapper.put("delete", asList(permissions));
		return this;
	}

	public RequestPermissionBuilder deleteByIdPermissions(UserPermissionType... permissions) {
		mapper.put("deleteById", asList(permissions));
		return this;
	}

	public boolean hasPermission(String method, List<UserPermissionType> permissions) {
		if (!mapper.containsKey(method)) {
			return false;
		}
		List<UserPermissionType> methodPermissions = mapper.get(method);
		return methodPermissions.containsAll(permissions);
	}

}
