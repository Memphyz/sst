package com.sst.repository.pcmso;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sst.model.pcmso.PCMSO;

@Repository
public interface PCMSORepository extends MongoRepository<PCMSO, String> {

}
