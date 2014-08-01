package fr.wati.yacramanager.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Company_;
import fr.wati.yacramanager.dao.repository.CompanyRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterDate;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;
import fr.wati.yacramanager.web.dto.CompanyDTO;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private ClientService clientService;

	@Override
	public <S extends Company> S save(S entity) {
		return companyRepository.save(entity);
	}

	@Override
	public <S extends Company> Iterable<S> save(Iterable<S> entities) {
		return companyRepository.save(entities);
	}

	@Override
	public Company findOne(Long id) {
		return companyRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return companyRepository.exists(id);
	}

	@Override
	public Iterable<Company> findAll() {
		return companyRepository.findAll();
	}

	@Override
	public Iterable<Company> findAll(Iterable<Long> ids) {
		return companyRepository.findAll(ids);
	}

	@Override
	public long count() {
		return companyRepository.count();
	}

	@Override
	public void delete(Long id) {
		companyRepository.delete(id);
	}

	@Override
	public void delete(Company entity) {
		companyRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Company> entities) {
		companyRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		companyRepository.deleteAll();
	}

	@Override
	public Company createCompany(Company company) {
		Company saveCompany = companyRepository.save(company);
		/*
		 * default client for the company each company should have at least one
		 * client we create a default one
		 */
		Client defaultClient = new Client();
		defaultClient.setName("Internal");
		clientService.createClient(saveCompany.getId(), defaultClient);
		return saveCompany;
	}

	@Override
	public CompanyDTO toCompanyDTO(Company company) {
		CompanyDTO companyDTO=new CompanyDTO();
		companyDTO.setId(company.getId());
		companyDTO.setName(company.getName());
		companyDTO.setRegisteredDate(company.getRegisteredDate());
		companyDTO.setContacts(company.getContacts());
		companyDTO.setLicenseEndDate(company.getLicenseEndDate());
		return companyDTO;
	}

	@Override
	public Page<Company> findAll(Pageable pageable) {
		return companyRepository.findAll(pageable);
	}

	@Override
	public Page<Company> findAll(Specification<Company> spec, Pageable pageable) {
		return companyRepository.findAll(spec, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.SpecificationFactory#buildSpecification(fr.wati.yacramanager.utils.Filter)
	 */
	@Override
	public Specification<Company> buildSpecification(Filter filter) {
		if(filter!=null){
			FilterType filterType = filter.getType();
			switch (filterType) {
			case TEXT:
				FilterText filterText=(FilterText) filter;
				if("name".equals(filterText.getField())){
					return CommonSpecifications.likeIgnoreCase(filterText.getValue(), Company_.name);
				}
				break;
			case DATE:
			case DATE_RANGE:
				FilterDate filterDate=(FilterDate) filter;
				if("registeredDate".equals(filter.getField())){
					if(filterDate.isRangedDate()){
						return CommonSpecifications.between(filterDate.getValue().getStart(), filterDate.getValue().getEnd(), Company_.registeredDate);
					}else {
						return CommonSpecifications.equals(filterDate.getValue().getDate(), Company_.registeredDate);
					}
				}
				if("licenseEndDate".equals(filter.getField())){
					if(filterDate.isRangedDate()){
						return CommonSpecifications.between(filterDate.getValue().getStart(), filterDate.getValue().getEnd(), Company_.licenseEndDate);
					}else {
						return CommonSpecifications.equals(filterDate.getValue().getDate(), Company_.licenseEndDate);
					}
				}
				break;
			default:
				break;
			}
		}
		return null;
	}

}
