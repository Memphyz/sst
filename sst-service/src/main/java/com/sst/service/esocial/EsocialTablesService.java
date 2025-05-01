package com.sst.service.esocial;

import static java.util.stream.Collectors.toList;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.sst.esocial.tables.HttpEsocialTables;
import com.sst.model.hazardous.agent.HazardousAgent;
import com.sst.repository.hazardous.gent.HazardousAgentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EsocialTablesService {

	@Autowired
	private HttpEsocialTables client;
	
	@Autowired
	private HazardousAgentRepository repository;

	public List<HazardousAgent> findAllHazardousAgent() {
		try {
			String html = client.getHtml();
			String tableStart = "<table id=\"24\"";
			String tableEnd = "</table>";
			int startIdx = html.indexOf(tableStart);
			int endIdx = html.indexOf(tableEnd, startIdx);
			String tableHtml = html.substring(startIdx, endIdx + tableEnd.length());

			// 2. Configura o parser
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			// 3. Parse como XML fragment (não requer documento completo)
			Document doc = builder
					.parse(new InputSource(new StringReader("<?xml version='1.0'?><root>" + tableHtml + "</root>")));
			Node tbody = doc.getElementsByTagName("tbody").item(0);
			List<Node> children = IntStream.range(0, tbody.getChildNodes().getLength()).mapToObj(tbody.getChildNodes()::item).collect(toList());
			List<HazardousAgent> agents = children.stream().map(child -> {
				List<String> data= Arrays.asList(child.getTextContent().split("\n")).stream().filter(content -> content.length() > 1).collect(toList());
				if(data.size() > 0 && data.get(0).matches("^\\d{2}\\.\\d{2}\\.\\d+$")) {
					return new HazardousAgent(data.get(0).replace(".", ""), data.get(1));
				}
				return null;
			}).filter(content -> content != null).collect(toList());
			return agents;
		} catch (Exception e) {
			log.error("EsocialTablesService | getTable | An error ocurred on attempt to get a table 24 from Esocial");
			e.printStackTrace();
		}
		return new ArrayList<HazardousAgent>();
	}
	
	public void saveAll(List<HazardousAgent> entities) {
		repository.deleteAll();
		repository.insert(entities);
	}
}
