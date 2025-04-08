package com.sst.abstracts.service;

import static org.springframework.data.domain.PageRequest.of;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.abstracts.model.AbstractModel;

public abstract class AbstractService<Document extends AbstractModel<?>, Repository extends MongoRepository<Document, ?>>{
	
	@Autowired
	protected Repository repository;
	
	@SuppressWarnings("unchecked")
	private <ID> MongoRepository<Document, ID> getDefaultRepository() {
		return (MongoRepository<Document, ID>) this.repository;
	}
	
	public <ID> Document findById(ID id) {
		return getDefaultRepository().findById(id).orElse(null);
	}
	
	public Page<Document> findAllBy(Integer page, Integer size, Map<String, String> params){
		Query query = new Query();
		
		for(Map.Entry<String, String> param : params.entrySet()) {
			query.addCriteria(Criteria.where(param.getKey()).is(param.getValue()));
		}
		
		Pageable pageable = of(page, size);
		Page<Document> documents = getDefaultRepository().findAll(pageable);
		return documents;
	}
	
	public void update(Document entity) {
		getDefaultRepository().save(entity);
	}
	
	public void delete(Document entity) {
		getDefaultRepository().delete(entity);
	}
	
	public <ID> void deleteById(ID id) {
		getDefaultRepository().deleteById(id);
	}
	
	public void save(Document entity) {
		getDefaultRepository().save(entity);
	}
	
	public Long count() {
		return getDefaultRepository().count();
	}
	
	public void saveAll(List<Document> entity) {
		getDefaultRepository().saveAll(entity);
	}
	
	public void deleteAll(List<Document> entity) {
		getDefaultRepository().deleteAll(entity);
	}
	
	public <ID> Boolean existsById(ID id) {
		return getDefaultRepository().existsById(id);
	}
	
	@SuppressWarnings("unchecked")
	public <ID> List<Document> findByIds(List<ID> id) {
		return getDefaultRepository().findAllById((Iterable<Object>) id);
	}
}
