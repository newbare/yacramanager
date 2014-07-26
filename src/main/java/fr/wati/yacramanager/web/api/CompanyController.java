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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.utils.Filter.FilterBuilder;
import fr.wati.yacramanager.utils.SpecificationBuilder;
import fr.wati.yacramanager.web.dto.CompanyDTO;
import fr.wati.yacramanager.web.dto.ResponseWrapper;

@RestController
@RequestMapping("/app/api/company")
public class CompanyController implements RestCrudController<CompanyDTO> {

	private static final Log LOG=LogFactory.getLog(CompanyController.class); 
	@Autowired
	private CompanyService companyService;
	
	
	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody CompanyDTO read(@PathVariable("id") Long id) {
		return companyService.toCompanyDTO(companyService.findOne(id));
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("id")Long id, @RequestBody CompanyDTO dto) {
		Company findOne = companyService.findOne(id);
		companyService.save(dto.toCompany(findOne));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@RequestMapping(method = RequestMethod.GET)
	public ResponseWrapper<List<CompanyDTO>> getAll(@RequestParam(required=false) Integer page,@RequestParam(required=false) Integer size,@RequestParam(value="sort", required=false) Map<String, String> sort,@RequestParam(value="filter", required=false) String filter) throws RestServiceException {
		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 100;
		}
		List filters=new ArrayList<>();
		if(StringUtils.isNotEmpty(filter)){
			try {
				filters=FilterBuilder.parse(filter);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				throw new RestServiceException(e);
			}
		}
		Specifications<Company> specifications=null;
		if(!filters.isEmpty()){
			LOG.debug("Building Absence specification");
			specifications=Specifications.where(SpecificationBuilder.buildSpecification(filters, companyService));
		}
		PageRequest pageable=null;
		if(sort!=null){
			List<Order> orders=new ArrayList<>();
			for(Entry<String, String> entry:sort.entrySet()){
				Order order=new Order("asc".equals(entry.getValue())?Direction.ASC:Direction.DESC, entry.getKey());
				orders.add(order);
			}
			if(!orders.isEmpty()){
				pageable=new PageRequest(page, size, new Sort(orders));
			}else {
				pageable=new PageRequest(page, size);
			}
		}else {
			pageable=new PageRequest(page, size);
		}
		
		Page<Company> findBySpecificationAndOrder =companyService.findAll(specifications, pageable);
		ResponseWrapper<List<CompanyDTO>> responseWrapper = new ResponseWrapper<>(
				DtoMapper.mapCompanies(findBySpecificationAndOrder),
				findBySpecificationAndOrder.getTotalElements());
		long startIndex=findBySpecificationAndOrder.getNumber()*size+1;
		long endIndex=startIndex+findBySpecificationAndOrder.getNumberOfElements()-1;
		responseWrapper.setStartIndex(startIndex);
		responseWrapper.setEndIndex(endIndex);
		return responseWrapper;
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestBody CompanyDTO dto) {
		companyService.createCompany(dto.toCompany(new Company()));
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@Override
	@RequestMapping(value = "/{id}",method=RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		companyService.delete(id);
	}

}
