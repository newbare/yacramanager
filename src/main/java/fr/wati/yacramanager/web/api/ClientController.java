package fr.wati.yacramanager.web.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Contact;
import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.ClientDTO;
import fr.wati.yacramanager.web.dto.ContactDTO;

@RestController
@RequestMapping("/app/api/{companyId}/client")
public class ClientController {

	private static final Log LOG = LogFactory.getLog(ClientController.class);

	@Inject
	private ClientService clientService;
	@Inject
	private CompanyService companyService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public ResponseEntity<ClientDTO> read(@PathVariable("companyId") Long companyId,
			@PathVariable("id") Long id) {
		Company company = companyService.findOne(companyId);
		if(company==null || clientService.findByCompanyAndId(
				company, id)==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(clientService.toClientDTO(clientService.findByCompanyAndId(
				company, id)),HttpStatus.OK);
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
	@PostFilter("filterObject.getCompanyId().equals(#companyId)")
	@Timed
	public List<ClientDTO> getAll(
			@PathVariable("companyId") @P("companyId") Long companyId,
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
		List<ClientDTO> clientDTOs = clientService.toClientDTOs(findBySpecificationAndOrder);
		long startIndex = findBySpecificationAndOrder.getNumber() * size + 1;
		long endIndex = startIndex
				+ findBySpecificationAndOrder.getNumberOfElements() - 1;
		
		return clientDTOs;
	}

	@RequestMapping(method = RequestMethod.POST)
	@Timed
	@RolesAllowed(Role.SSII_ADMIN)
	public ResponseEntity<String> create(
			@PathVariable("companyId") Long companyId, @RequestBody ClientDTO dto) {
		Company company = companyService.findOne(companyId);
		clientService.createClient(company.getId(), dto.toClient());
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	@RolesAllowed(Role.SSII_ADMIN)
	public void delete(@PathVariable("companyId") Long companyId,
			@PathVariable("id") Long id) {
		Company company = companyService.findOne(companyId);
		Client client = clientService.findByCompanyAndId(company, id);
		if (client != null) {
			clientService.delete(client);
		}
	}
	
	@RequestMapping(value = "/logo/{clientId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> downloadUserAvatarImage(
			@PathVariable Long clientId, HttpServletRequest httpServletRequest) {
		Client client = clientService.findOne(clientId);
		if (client != null && clientId != 0) {
			if (client.getLogo()!=null) {
				byte[] clientLogo = clientService.getLogo(clientId);
				if(clientLogo!=null){
					return ResponseEntity
							.ok()
							.contentType(MediaType.IMAGE_JPEG)
							.body(new InputStreamResource(
									new ByteArrayInputStream(clientLogo)));
				}
			}
		}
		InputStream defaultLogo = httpServletRequest.getServletContext()
				.getResourceAsStream("/assets/images/company/company-logo-default.jpg");
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
				.body(new InputStreamResource(defaultLogo));
	}

}
