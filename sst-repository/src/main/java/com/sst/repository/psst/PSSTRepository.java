package com.sst.repository.psst;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.model.psst.PSST;

public interface PSSTRepository extends MongoRepository<PSST, String> {

}
