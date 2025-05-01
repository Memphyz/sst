package com.sst.repository.hazardous.gent;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.model.hazardous.agent.HazardousAgent;

public interface HazardousAgentRepository extends MongoRepository<HazardousAgent, String>{

}
