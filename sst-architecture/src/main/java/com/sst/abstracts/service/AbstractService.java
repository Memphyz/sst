package com.sst.abstracts.service;

import static java.util.Arrays.asList;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.abstracts.model.AbstractModel;

public abstract class AbstractService<Document extends AbstractModel<?>, Repository extends MongoRepository<Document, ?>>{
	
	@Autowired
	protected Repository repository;
	
	@Autowired
	private MongoTemplate template;
	
	@SuppressWarnings("unchecked")
	private MongoRepository<Document, Object> getDefaultRepository() {
		return (MongoRepository<Document, Object>) this.repository;
	}
	
	public <ID> Document findById(ID id) {
		return getDefaultRepository().findById(id).orElse(null);
	}
	
	public List<Document> findAllBy(Map<String, String> params){
		Query query = new Query();
		
		for(Map.Entry<String, String> param : params.entrySet()) {
			query.addCriteria(Criteria.where(param.getKey()).is(param.getValue()));
			
		}
		@SuppressWarnings("unchecked")
		Class<Document> documented = (Class<Document>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
		List<Document> documents = template.find(query, documented);
		return ObjectUtils.defaultIfNull(documents, asList());
	}
	
}
