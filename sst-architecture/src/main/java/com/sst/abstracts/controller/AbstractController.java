package com.sst.abstracts.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.sst.abstracts.model.AbstractModel;
import com.sst.abstracts.service.AbstractService;
import com.sst.exceptions.ResourceNotFound;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
	public ResponseEntity<?> getAllBy(
			@RequestParam(defaultValue = "0") final Integer page,
	        @RequestParam(defaultValue = "5") final Integer size,
			@RequestParam Map<String, String> params) {
		Page<Document> documents = service.findAllBy(page, size, params);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("X-Content-Range", documents.getSize() + "/" + documents.getTotalElements());
		responseHeaders.set("X-Content-Pages", String.valueOf(documents.getTotalPages()));
		return new ResponseEntity<>(documents.toList(), responseHeaders, OK);
	}
	
	@PostMapping
	@Operation(description = "Save a document")
	public ResponseEntity<?> save(@RequestBody @Valid Document entity) {
		service.save(entity);
		return ok(entity);
	}
	
	@DeleteMapping
	@Operation(description = "Save a document")
	public ResponseEntity<?> delete(@RequestBody Document entity) {
		service.delete(entity);
		return ok().build();
	}
	
	@DeleteMapping("/{id}")
	@Operation(description = "Save a document")
	public ResponseEntity<?> deleteById(@PathVariable String id) {
		service.deleteById(id);
		return ok().build();
	}
}
