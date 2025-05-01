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
	
	@Scheduled(cron = "0 0 9 * * ?", zone = "America/Sao_Paulo")
	public void updateHazardousAgentCollection() {
		log.info("HazardousAgentSchedule | Scheduled | updateHazardousAgentCollection | Updating Hazardous Agent collection");
		List<HazardousAgent> agents = service.findAllHazardousAgent();
		service.saveAll(agents);
	}

}
