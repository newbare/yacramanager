package fr.wati.yacramanager.web.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Contact;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.ClientDTO;
import fr.wati.yacramanager.web.dto.ContactDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

@RestController
@RequestMapping("/app/api/{companyId}/client")
public class ClientController {

	private static final Log LOG = LogFactory.getLog(ClientController.class);

	@Autowired
	private ClientService clientService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private DtoMapper dtoMapper;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public ClientDTO read(@PathVariable("companyId") Long companyId,
			@PathVariable("id") Long id) {
		Company company = companyService.findOne(companyId);
		return clientService.toClientDTO(clientService.findByCompanyAndId(
				company, id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Timed
	public ResponseEntity<String> update(
			@PathVariable("companyId") Long companyId,
			@PathVariable("id") Long id, @RequestBody ClientDTO dto) {
		Company company = companyService.findOne(companyId);
		Client client = clientService.findByCompanyAndId(company, id);
		if (client != null) {
			dto.toClient(client);
			List<Contact> contacts=new ArrayList<>();
			for (ContactDTO contactDTO : dto.getContacts()) {
				contacts.add(contactDTO.toContact());
			}
			client.setContacts(contacts);
			clientService.save(client);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>("Client with id: " + id
				+ " and company id: " + companyId + " does not exist",
				HttpStatus.NOT_MODIFIED);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	@Timed
	public ResponseWrapper<List<ClientDTO>> getAll(
			@PathVariable("companyId") Long companyId,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false, value = "sort") Map<String, String> sort,
			@RequestParam(value = "filter", required = false) String filter)
			throws RestServiceException {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 100;
		}
		List filters = new ArrayList<>();
		if (StringUtils.isNotEmpty(filter)) {
			try {
				filters = FilterBuilder.parse(filter);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				throw new RestServiceException(e);
			}
		}
		Specifications<Client> specifications = null;
		if (!filters.isEmpty()) {
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, clientService));
		}
		PageRequest pageable = null;
		if (sort != null) {
			List<Order> orders = new ArrayList<>();
			for (Entry<String, String> entry : sort.entrySet()) {
				Order order = new Order(
						"asc".equals(entry.getValue()) ? Direction.ASC
								: Direction.DESC, entry.getKey());
				orders.add(order);
			}
			if (!orders.isEmpty()) {
				pageable = new PageRequest(page, size, new Sort(orders));
			} else {
				pageable = new PageRequest(page, size);
			}
		} else {
			pageable = new PageRequest(page, size);
		}

		Page<Client> findBySpecificationAndOrder = clientService.findAll(
				specifications, pageable);
		ResponseWrapper<List<ClientDTO>> responseWrapper = new ResponseWrapper<>(
				clientService.toClientDTOs(findBySpecificationAndOrder),
				findBySpecificationAndOrder.getTotalElements());
		long startIndex = findBySpecificationAndOrder.getNumber() * size + 1;
		long endIndex = startIndex
				+ findBySpecificationAndOrder.getNumberOfElements() - 1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}

	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> create(
			@PathVariable("companyId") Long companyId, @RequestBody ClientDTO dto) {
		Company company = companyService.findOne(companyId);
		clientService.createClient(company.getId(), dto.toClient());
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	public void delete(@PathVariable("companyId") Long companyId,
			@PathVariable("id") Long id) {
		Company company = companyService.findOne(companyId);
		Client client = clientService.findByCompanyAndId(company, id);
		if (client != null) {
			clientService.delete(client);
		}
	}

}
