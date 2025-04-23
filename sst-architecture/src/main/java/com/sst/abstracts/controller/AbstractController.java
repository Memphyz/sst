package com.sst.abstracts.controller;

import static com.sst.enums.ValidationMessagesType.NOT_NULL;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
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
		log.info("GET | {} | getAllBy | Buscando documentos na pagina {} com o tamanho maximo de {}", name, page, size);
		Page<Document> documents = service.findAllBy(page, size, params);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("X-Content-Range", documents.getSize() + "/" + documents.getTotalElements());
		responseHeaders.set("X-Content-Pages", String.valueOf(documents.getTotalPages()));
		log.info("GET | {} | getAllBy | Encontrados {} documentos do total de {}", name, documents.getSize(),
				documents.getTotalElements());
		return new ResponseEntity<>(documents.toList(), responseHeaders, OK);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping
	@Operation(description = "Save or update a document")
	public ResponseEntity<?> save(@RequestBody @Valid Document entity) {
		log.info("POST | {} | save | Verificando se documento já existe", name);
		Boolean exist = service.findById(entity.getId()) != null;
		if(!exist) {
			Document saved = service.save(entity);
			if (entity instanceof AbstractModelAuditable) {
				AbstractModelAuditable object = (AbstractModelAuditable) entity;
				log.info("POST | {} | save | Documento {} salvo com sucesso pelo usuário {}.", name, entity.getId(),
						object.getCreatedBy());
			} else {
				log.info("POST | {} | save | Documento {} salvo com sucesso.", name, entity.getId());
			}
			return new ResponseEntity<>(saved, CREATED);
		} else {
			log.info("POST | {} | save | Documento de id {} já existe, executando update...", name, entity.getId());
			Document updated = service.update(entity);
			return new ResponseEntity<>(updated, OK);
		}
	}
	
	@PostMapping("/all")
	@Operation(description = "Save or update multiple documents")
	public ResponseEntity<?> saveAll(@RequestBody @Valid List<Document> entities) {
		log.info("POST | {} | saveAll | Verificando documentos já existentes e documentos novos", name);
		List<Document> unsaved = entities.stream().filter(entity -> !service.existsById(entity.getId())).collect(toList());
		List<Document> saved = entities.stream().filter(entity -> !unsaved.contains(entity)).collect(toList());
		List<Document> documents = new ArrayList<Document>();
		if(!unsaved.isEmpty()) {
			log.info("POST | {} | saveAll | Salvando {} novos documentos", name, unsaved.size());
			documents.addAll(service.save(unsaved));
			log.info("POST | {} | saveAll | {} documentos salvos com sucesso!", name, unsaved.size());
		}
		if(!saved.isEmpty()) {
			log.info("POST | {} | saveAll | Atualizando {} novos documentos", name, saved.size());
			documents.addAll(service.update(saved));
			log.info("POST | {} | saveAll | {} documentos atualizados com sucesso!", name, saved.size());
		}
		return ok(documents);
	}
	
	@DeleteMapping("/all")
	@Operation(description = "Delete multiple documents by document body or by ids list. If a list of ids was provided, the method will prioritize ids by performatic reasons")
	public ResponseEntity<?> deleteAll(
			@RequestBody @Nullable @Valid List<Document> entities,
			@RequestParam @Nullable List<String> ids) {
		if(ids != null && !ids.isEmpty()) {
			log.info("DELETE | {} | deleteAll | Deletando {} documentos por ids", name, ids.size());
			service.deleteAllByIds(ids);
			log.info("DELETE | {} | deleteAll | {} documentos deletados por id com sucesso!", name, ids.size());
			return ok().build();
		}
		if(entities != null && !entities.isEmpty()) {
			log.info("DELETE | {} | deleteAll | Deletando {} documentos", name, entities.size());
			service.deleteAll(entities);
			log.info("DELETE | {} | deleteAll | {} documentos deletados com sucesso!", name, entities.size());
			return ok().build();
		}
		throw new ResourceNotFound(NOT_NULL);
	}
	
	@PostMapping("/update")
	@Operation(description = "Update a document")
	public ResponseEntity<?> update(@RequestBody @Valid Document entity) {
		log.info("POST | {} | update | Atualizando o documento com o id: {}", name, entity.getId());
		Document updated = service.update(entity);
		if (updated instanceof AbstractModelAuditable) { 
			@SuppressWarnings("rawtypes")
			AbstractModelAuditable object = (AbstractModelAuditable) entity;
			log.info("POST | {} | update | Documento {} atualizado com sucesso pelo usuário {}!", name, entity.getId(), object.getUpdatedBy());
			return new ResponseEntity<>(updated, OK);
		}
		log.info("POST | {} | update | Documento {} atualizado com sucesso!", name, entity.getId());
		return new ResponseEntity<>(updated, OK);
	}
	
	@DeleteMapping
	@Operation(description = "Delete a document")
	public ResponseEntity<?> delete(@RequestBody Document entity) {
		log.info("DELETE | {} | delete | Deletando documento", name);
		service.delete(entity);
		log.info("DELETE | {} | delete | Documento {} deletado com sucesso!", name, entity.getId());
		return ok().build();
	}

	@DeleteMapping("/{id}")
	@Operation(description = "Delete a document by id")
	public ResponseEntity<?> deleteById(@PathVariable String id) {
		log.info("DELETE | {} | deleteById | Deletando documento pelo id {}", name, id);
		service.deleteById(id);
		log.info("DELETE | {} | deleteById | Documento de id {} deletado com sucesso!", name, id);
		return ok().build();
	}
}
