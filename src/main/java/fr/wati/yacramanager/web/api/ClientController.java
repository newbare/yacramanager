package fr.wati.yacramanager.web.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.wati.yacramanager.web.dto.ClientDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

@RequestMapping("/app/api/client")
public class ClientController {

	@RequestMapping(value = "/{companyId}/{id}", method = RequestMethod.GET)
	public ClientDTO read(@PathVariable("companyId") Long companyId, @PathVariable("id")Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/{companyId}/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable("companyId") Long companyId,@PathVariable("id")Long id, ClientDTO dto) {
		// TODO Auto-generated method stub
		
	}

	public ResponseWrapper<List<ClientDTO>> getAll(Integer page,
			Integer Integer, String orderBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/{companyId}",method = RequestMethod.POST)
	public ResponseEntity<String> create(@PathVariable("companyId") Long companyId,ClientDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/{companyId}/{id}",method=RequestMethod.DELETE)
	public void delete(@PathVariable("companyId") Long companyId,@PathVariable("id") Long id) {
		// TODO Auto-generated method stub
		
	}

}
