package fr.wati.yacramanager.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.web.dto.CompanyDTO;

public interface CompanyService extends CrudService<Company, Long>,SpecificationFactory<Company> {

	Company createCompany(Company company);

	CompanyDTO toCompanyDTO(Company company);
	
	Page<Company> findAll(Pageable pageable);
}
