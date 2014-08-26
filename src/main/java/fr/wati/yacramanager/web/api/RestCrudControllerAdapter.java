/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import fr.wati.yacramanager.services.SpecificationFactory;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

/**
 * @author Rachid Ouattara
 *
 */
public class RestCrudControllerAdapter<DTO> implements RestCrudController<DTO> {

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.rest.RestCrudController#read(java.lang.Long)
	 */
	@Override
	public ResponseEntity<DTO> read(Long id) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.rest.RestCrudController#update(java.lang.Long, java.lang.Object)
	 */
	@Override
	public void update(Long id, DTO dto) {
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.rest.RestCrudController#getAll(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ResponseWrapper<List<DTO>> getAll(Integer page,Integer size,Map<String, String> sort,String filter) throws RestServiceException{
		return null;
	}
	
	public <SERVICE> ResponseWrapper<List<DTO>> getAll(Integer page,Integer size,Map<String, String> sort,String filter,SpecificationFactory<SERVICE> specificationService,Class<SERVICE> serviceClass) {
		return null;
	}

	
	
	
	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.rest.RestCrudController#create(java.lang.Object)
	 */
	@Override
	public ResponseEntity<String> create(DTO dto) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.web.rest.RestCrudController#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long ID) {
	}

}
