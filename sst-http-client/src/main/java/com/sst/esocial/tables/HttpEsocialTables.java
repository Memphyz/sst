package com.sst.esocial.tables;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "https://www.gov.br/esocial/pt-br/", name = "tabelas")
public interface HttpEsocialTables {
	
	@GetMapping("documentacao-tecnica/manuais/leiautes-esocial-v-1-1-beta/tabelas.html")
	String getHtml();

}
