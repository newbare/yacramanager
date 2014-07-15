package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.web.dto.ClientDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

@RestController
@RequestMapping("/app/api/{companyId}/client")
public class ClientController {

	@Autowired
	private ClientService clientService;
	@Autowired
	private CompanyService companyService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ClientDTO read(@PathVariable("companyId") Long companyId, @PathVariable("id")Long id) {
		Company company = companyService.findOne(companyId);
		return clientService.toClientDTO(clientService.findByCompanyAndId(company, id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> update(@PathVariable("companyId") Long companyId,@PathVariable("id")Long id, ClientDTO dto) {
		Company company = companyService.findOne(companyId);
		Client client = clientService.findByCompanyAndId(company, id);
		if(client!=null){
			dto.toClient(client);
			clientService.save(client);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>("Client with id: "+id+" and company id: "+companyId+" does not exist",HttpStatus.NOT_MODIFIED);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseWrapper<List<ClientDTO>> getAll(@PathVariable("companyId") Long companyId,@RequestParam(required=false) Integer page,@RequestParam(required=false) Integer size,@RequestParam(required=false,defaultValue="id") String orderBy) {
		if(page==null){
			page=0;
		}
		if(size==null){
			size=100;
		}
		Company company=companyService.findOne(companyId);
		if(company!=null){
			PageRequest pageable=new PageRequest(page, size,new Sort(new Order(Direction.DESC,orderBy)));
			Page<Client> findPage = clientService.findByCompany(company,pageable);
			return new ResponseWrapper<List<ClientDTO>>(DtoMapper.mapClients(findPage),findPage.getTotalElements());
		}
		return new ResponseWrapper<List<ClientDTO>>(new ArrayList<ClientDTO>(),0);
	}

	@RequestMapping(value = "/",method = RequestMethod.POST)
	public ResponseEntity<String> create(@PathVariable("companyId") Long companyId,ClientDTO dto) {
		Company company = companyService.findOne(companyId);
		clientService.createClient(company.getId(), dto.toClient());
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}",method=RequestMethod.DELETE)
	public void delete(@PathVariable("companyId") Long companyId,@PathVariable("id") Long id) {
		Company company = companyService.findOne(companyId);
		Client client=clientService.findByCompanyAndId(company, id);
		if(client!=null){
			clientService.delete(client);
		}
	}

}
