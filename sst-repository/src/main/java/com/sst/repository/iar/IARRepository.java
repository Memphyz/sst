package com.sst.repository.iar;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.model.iar.IAR;

public interface IARRepository extends MongoRepository<IAR, String>{

}
