package com.sst.abstracts.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.abstracts.resource.AbstractResource;
import com.sst.abstracts.service.AbstractService;
import com.sst.exceptions.ResourceNotFound;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractController<Document extends AbstractModel<?>, Resource extends AbstractResource<Document, Resource>, Service extends AbstractService<Document, ? extends MongoRepository<Document, ?>>> {

	@Autowired
	protected Service service;

	private final String name = this.getClass().getSimpleName();
	private Resource resource = getResource();

	@SuppressWarnings("unchecked")
	private Resource getResource() {
		Type superclass = getClass().getGenericSuperclass();
		ParameterizedType type = (ParameterizedType) superclass;
		Type[] typeArguments = type.getActualTypeArguments();
		Type resourceType = typeArguments[1];
		Class<Resource> resource = (Class<Resource>) resourceType;
		try {
			return resource.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	@GetMapping("/{id}")
	@Operation(description = "A default getter document by id")
	public ResponseEntity<?> getById(@NotNull @PathVariable String id) {
		log.info("GET | {} | getById | Buscando documento de id {}", name, id);
		Document document = service.findById(id);
		if (document == null) {
			log.info("GET | {} | getById | documento de id {} não encontrado", name, id);
			throw new ResourceNotFound();
		}
		log.info("GET | {} | getById | Buscando documento de id {} encontrado", name, id);
		return ok(resource.copy(document));
	}

	@GetMapping
	@Operation(description = "Paginated list of documents with custom parameters")
	public ResponseEntity<?> getAllBy(@RequestParam(defaultValue = "0") final Integer page,
			@RequestParam(defaultValue = "5") final Integer size, @RequestParam Map<String, String> params) {
		log.info("GET | {} | getById | Buscando documentos na pagina {} com o tamanho maximo de {}", name, page, size);
		Page<Document> documents = service.findAllBy(page, size, params);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("X-Content-Range", documents.getSize() + "/" + documents.getTotalElements());
		responseHeaders.set("X-Content-Pages", String.valueOf(documents.getTotalPages()));
		log.info("GET | {} | getById | Encontrados {} documentos do total de {}", name, documents.getSize(),
				documents.getTotalElements());
		return new ResponseEntity<>(documents.toList(), responseHeaders, OK);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping
	@Operation(description = "Save a document")
	public ResponseEntity<?> save(@RequestBody @Valid Document entity) {
		log.info("GET | {} | getById | Salvando documento de id {}", name, entity.getId());
		service.save(entity);
		if (entity instanceof AbstractModelAuditable) {
			AbstractModelAuditable object = (AbstractModelAuditable) entity;
			log.info("GET | {} | getById | Documento {} salvo com sucesso pelo usuário {}.", name, entity.getId(),
					object.getCreatedBy());
		} else {
			log.info("GET | {} | getById | Documento {} salvo com sucesso.", name, entity.getId());
		}
		return ok(entity);
	}

	@DeleteMapping
	@Operation(description = "Delete a document")
	public ResponseEntity<?> delete(@RequestBody Document entity) {
		log.info("GET | {} | getById | Deletando documento", name);
		service.delete(entity);
		log.info("GET | {} | getById | Documento {} deletado com sucesso!", name, entity.getId());
		return ok().build();
	}

	@DeleteMapping("/{id}")
	@Operation(description = "Delete a document by id")
	public ResponseEntity<?> deleteById(@PathVariable String id) {
		log.info("GET | {} | getById | Deletando documento pelo id {}", name, id);
		service.deleteById(id);
		log.info("GET | {} | getById | Documento de id {} deletado com sucesso!", name, id);
		return ok().build();
	}
}
