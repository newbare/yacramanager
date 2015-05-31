/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Invoice;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.InvoiceService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.api.utils.PaginationUtil;
import fr.wati.yacramanager.web.dto.InvoiceDTO;

/**
 * @author Rachid Ouattara
 *
 */
@RestController
@RequestMapping("/app/api/invoices")
public class InvoiceController {

	private static final Log LOG = LogFactory.getLog(InvoiceController.class);
	@Inject
	private InvoiceService invoiceService;
	@Inject
	private ClientService clientService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@Timed
	@PostFilter("principal.getDomainUser().getCompany().getId().equals(filterObject.getClient().getCompanyId())")
	public List<InvoiceDTO> getAll(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false, value = "sort") Map<String, String> sort,
			@RequestParam(value = "filter", required = false) String filter,
			HttpServletResponse httpServletResponse)
			throws RestServiceException, URISyntaxException {
		List filters = new ArrayList<>();
		if (StringUtils.isNotEmpty(filter)) {
			try {
				filters = FilterBuilder.parse(filter);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				throw new RestServiceException(e);
			}
		}
		Specifications<Invoice> specifications = null;
		if (!filters.isEmpty()) {
			//LOG.debug("Building Task specification");
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, invoiceService));
		}
		
		Page<Invoice> findBySpecificationAndOrder = invoiceService.findAll(specifications, PaginationUtil.generatePageRequest(page, size, sort));
		List<InvoiceDTO> response = invoiceService
				.toInvoiceDTOs(findBySpecificationAndOrder);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(findBySpecificationAndOrder, "/app/api/invoices/all", page, size);
		PaginationUtil.enhanceHttpServletResponse(headers, httpServletResponse);
		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public ResponseEntity<InvoiceDTO> read(
			@PathVariable("id") Long id) {
		return new ResponseEntity<InvoiceDTO>(invoiceService.toInvoiceDTO(invoiceService
				.findOne(id)), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> update(@PathVariable("id") Long id,
			@RequestBody InvoiceDTO dto) {
		Invoice invoice=invoiceService.findOne(id);
		if (invoice != null) {
			invoiceService.save(invoiceService.fromDTO(invoice, dto));
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>("The given invoice id:" + id
				+ " does not exist", HttpStatus.NOT_MODIFIED);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> create(@RequestBody InvoiceDTO dto) throws ServiceException {
		Invoice invoiceToCreate = dto.toInvoice(new Invoice());
		Client client = clientService.findOne(dto.getClient().getId());
		invoiceToCreate.setClient(client);
		Invoice invoice = invoiceService.save(invoiceToCreate);
		return new ResponseEntity<String>(String.valueOf(invoice.getId()),HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	public void delete(@PathVariable("id") @P("id") Long id) {
		invoiceService.delete(id);
	}
}
