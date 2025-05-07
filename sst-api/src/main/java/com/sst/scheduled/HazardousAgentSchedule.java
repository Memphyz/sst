package com.sst.scheduled;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sst.model.hazardous.agent.HazardousAgent;
import com.sst.service.esocial.EsocialTablesService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HazardousAgentSchedule {
	
	@Autowired
	private EsocialTablesService service;
	
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;
	
	@Scheduled(fixedDelay = ONE_DAY)
	public void updateHazardousAgentCollection() {
		log.info("HazardousAgentSchedule | Scheduled | updateHazardousAgentCollection | Updating Hazardous Agent collection");
		List<HazardousAgent> agents = service.findAllHazardousAgent();
		service.saveAll(agents);
	}

}
