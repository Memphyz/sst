package com.sst.repository.ppra;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sst.model.ppra.PPRA;

public interface PPRARepository extends MongoRepository<PPRA, String>{

}
