package fr.wati.yacramanager.web.api;

import java.util.List;

import org.springframework.http.ResponseEntity;

import fr.wati.yacramanager.web.dto.ResponseWrapper;

public interface RestCrudController<DTO> {

	DTO read(Long id);

	void update(Long id, DTO dto);
	
	ResponseWrapper<List<DTO>> getAll(Integer page,Integer Integer,String orderBy);

	ResponseEntity<String> create(DTO dto);

	void delete(Long ID);

}