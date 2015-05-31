package fr.wati.yacramanager.web.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

import com.codahale.metrics.annotation.Timed;
import com.mangofactory.swagger.annotations.ApiIgnore;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.CompanyTempInvitation;
import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.dao.JdbcCompanyInvitationRepository;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.MailService;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.CompanyDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

@RestController
@ApiIgnore
@RequestMapping("/app/api/company")
public class CompanyController {

	private static final Log LOG = LogFactory.getLog(CompanyController.class);
	@Inject
	private CompanyService companyService;
	@Inject
	private DtoMapper dtoMapper;

	@Inject
	private MailService mailService;

	@Resource(name="templateEngine")
	private SpringTemplateEngine templateEngine;
	
	@Inject
	private JdbcCompanyInvitationRepository companyInvitationRepository;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Timed
	public @ResponseBody
	ResponseEntity<CompanyDTO> read(@PathVariable("id") Long id) {
		if (companyService.exists(id)) {
			return new ResponseEntity<CompanyDTO>(
					companyService.toCompanyDTO(companyService.findOne(id)),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RolesAllowed({Role.ADMIN,Role.SSII_ADMIN})
	@Timed
	public void update(@PathVariable("id") Long id, @RequestBody CompanyDTO dto) {
		Company findOne = companyService.findOne(id);
		companyService.save(dto.toCompany(findOne));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	@Timed
	public ResponseWrapper<List<CompanyDTO>> getAll(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(value = "sort", required = false) Map<String, String> sort,
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
		Specifications<Company> specifications = null;
		if (!filters.isEmpty()) {
			LOG.debug("Building Absence specification");
			specifications = Specifications.where(SpecificationBuilder
					.buildSpecification(filters, companyService));
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

		Page<Company> findBySpecificationAndOrder = companyService.findAll(
				specifications, pageable);
		ResponseWrapper<List<CompanyDTO>> responseWrapper = new ResponseWrapper<>(
				dtoMapper.mapCompanies(findBySpecificationAndOrder),
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
	@RolesAllowed(Role.ADMIN)
	public ResponseEntity<String> create(@RequestBody CompanyDTO dto) {
		companyService.createCompany(dto.toCompany(new Company()));
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RolesAllowed({ Role.ADMIN })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Timed
	public void delete(@PathVariable("id") Long id) {
		companyService.delete(id);
	}

	@RequestMapping(value = "/{id}/invite", method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> inviteEmploye(
			@PathVariable("id") Long companyId,
			@RequestBody(required = true) CompanyTempInvitation invitation,
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {
		CompanyTempInvitation findInvitation = companyInvitationRepository
				.findInvitation(invitation.getUserId(),
						String.valueOf(companyId));
		if (findInvitation == null) {
			Company company = companyService.findOne(companyId);
			CompanyTempInvitation addInvitation = companyInvitationRepository
					.addInvitation(invitation.getUserId(),
							String.valueOf(companyId), company.getName(),
							invitation.getFirstName(), invitation.getLastName());
			// TODO Send invitation by mail
			String content = createHtmlContentFromTemplate(addInvitation,
					locale, request, response);
			mailService.sendActivationEmail(addInvitation.getUserId(), content, locale);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		return new ResponseEntity<String>("Invitation already exist !!",
				HttpStatus.OK);
	}
	
	private String createHtmlContentFromTemplate(final CompanyTempInvitation companyTempInvitation,
			final Locale locale, final HttpServletRequest request,
			final HttpServletResponse response) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("firstName", companyTempInvitation.getFirstName());
		variables.put("lastName", companyTempInvitation.getLastName());
		variables.put("invitationToken", companyTempInvitation.getToken());
		variables.put("invitationUsername", companyTempInvitation.getUserId());
		variables.put("invitationCompany", companyTempInvitation.getCompanyId());
		variables.put("baseUrl", request.getScheme() + "://" + // "http" + "://
				request.getServerName() + // "myhost"
				":" + request.getServerPort()+request.getContextPath());
		IWebContext context = new SpringWebContext(request, response,
				request.getServletContext(), locale, variables,
				WebApplicationContextUtils.getWebApplicationContext(request
						.getServletContext()));
		return templateEngine.process("companyInvitationEmail", context);
	}
	@RequestMapping(value = "/logo/{companyId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<InputStreamResource> downloadUserAvatarImage(
			@PathVariable Long companyId, HttpServletRequest httpServletRequest) {
		Company company = companyService.findOne(companyId);
		if (company != null && companyId != 0) {
			if (company.getLogo()!=null) {
				byte[] companyLogo = companyService.getLogo(companyId);
				if(companyLogo!=null){
					return ResponseEntity
							.ok()
							.contentType(MediaType.IMAGE_JPEG)
							.body(new InputStreamResource(
									new ByteArrayInputStream(companyLogo)));
				}
			}
		}
		InputStream defaultLogo = httpServletRequest.getServletContext()
				.getResourceAsStream("/assets/images/company/company-logo-default.jpg");
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
				.body(new InputStreamResource(defaultLogo));
	}
	
	@RequestMapping(value="/logo/{companyId}",method = RequestMethod.POST)
	@Timed
	public ResponseEntity<String> updateLogo(@PathVariable("companyId") Long companyId, @RequestParam("file") MultipartFile file) {
		try {
			if (file != null) {
				Company company=companyService.findOne(companyId);
				company.setLogo(IOUtils.toByteArray(file
						.getInputStream()));
				companyService.save(company);
				return new ResponseEntity<>( HttpStatus.OK);
			}
			return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
		} catch (Exception exception) {
			LOG.error(exception.getMessage(), exception);
			return new ResponseEntity<String>(exception.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
