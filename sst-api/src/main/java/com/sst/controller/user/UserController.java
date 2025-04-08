package com.sst.controller.user;

import static com.sst.constants.UrlMappingConstants.V1;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sst.model.user.User;
import com.sst.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(description = "findByEmail, deleteById, update, etc...", name = "User API")
@RequestMapping(V1 + "/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Operation(description = "Find a user by e-mail")
	@GetMapping("/email/{email}")
	public ResponseEntity<?> findByEmail(@PathVariable @NotNull @Parameter(description = "A user email also used by user ID") String email) {
		User user = service.loadUserByUsername(email);
		if(user == null) {
			return status(NOT_FOUND).build();
		}
		return ok(user);
	}
	
	@Operation(description = "Delete user by e-mail")
	@DeleteMapping("/email/{email}")
	public void deleteByEmail(@PathVariable @NotNull @Parameter(description = "A user email also used by user ID") String email) {
		service.deleteByEmail(email);
	}

}
