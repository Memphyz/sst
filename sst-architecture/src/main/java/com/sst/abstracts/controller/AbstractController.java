package com.sst.abstracts.controller;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sst.abstracts.model.AbstractModel;
import com.sst.abstracts.service.AbstractService;
import com.sst.exceptions.ResourceNotFound;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractController<Document extends AbstractModel<?>, Service extends AbstractService<Document, ? extends MongoRepository<Document,?>>> {
	
	@Autowired
	protected Service service;
	
	@GetMapping("/{id}")
	@Operation(description = "A default getter document by id")
	public ResponseEntity<?> getById(@NotNull @PathVariable String id) {
		Document document = service.findById(id);
		if(document == null) {
			throw new ResourceNotFound();
		}
		return ok(document);
	}
	
	@GetMapping
	@Operation(description = "Paginated list of documents")
	public ResponseEntity<?> getAllBy(@RequestParam Map<String, String> params) {
		List<Document> documents = service.findAllBy(params);
		return ok(documents);
	}
}
