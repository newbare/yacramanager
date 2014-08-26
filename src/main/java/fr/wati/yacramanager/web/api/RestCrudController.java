package fr.wati.yacramanager.web.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import fr.wati.yacramanager.web.dto.ResponseWrapper;

public interface RestCrudController<DTO> {

	ResponseEntity<DTO> read(Long id) throws RestServiceException;

	void update(Long id, DTO dto) throws RestServiceException;
	
	ResponseWrapper<List<DTO>> getAll(Integer page,Integer Integer,Map<String, String> sort,String filter) throws RestServiceException;

	ResponseEntity<String> create(DTO dto) throws RestServiceException;

	void delete(Long id) throws RestServiceException;

}