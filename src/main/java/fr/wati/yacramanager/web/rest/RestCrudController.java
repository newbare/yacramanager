package fr.wati.yacramanager.web.rest;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.ResponseEntity;

public interface RestCrudController<DTO, ID extends Serializable> {

	DTO read(ID id);

	void update(ID id, DTO dto);
	
	List<DTO> getAll();

	ResponseEntity<String> create(DTO dto);

	void delete(long ID);

}