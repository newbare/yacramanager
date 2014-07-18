package fr.wati.yacramanager.web.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import fr.wati.yacramanager.web.dto.ResponseWrapper;

public interface RestCrudController<DTO> {

	DTO read(Long id);

	void update(Long id, DTO dto);
	
	ResponseWrapper<List<DTO>> getAll(Integer page,Integer Integer,Map<String, String> sort,Map<String, String> filter);

	ResponseEntity<String> create(DTO dto);

	void delete(Long id);

}