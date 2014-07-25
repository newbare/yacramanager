package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Civilite;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.dao.repository.UsersRepository;
import fr.wati.yacramanager.dao.specifications.EmployeSpecifications;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterDate;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;

@Transactional
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private CompanyService companyService;
	
	public UserServiceImpl() {
	}

	@Override
	public <S extends Users> S save(S entity) {
		return usersRepository.save(entity);
	}

	@Override
	public <S extends Users> Iterable<S> save(Iterable<S> entities) {
		return usersRepository.save(entities);
	}

	@Override
	@Transactional(readOnly=true)
	public Users findOne(Long id) {
		return usersRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean exists(Long id) {
		return usersRepository.exists(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<Users> findAll() {
		return usersRepository.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<Users> findAll(Iterable<Long> ids) {
		return usersRepository.findAll(ids);
	}

	@Override
	@Transactional(readOnly=true)
	public long count() {
		return usersRepository.count();
	}

	@Override
	public void delete(Long id) {
		usersRepository.delete(id);
	}

	@Override
	public void delete(Users entity) {
		usersRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Users> entities) {
		usersRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		usersRepository.deleteAll();
	}

	public Users findByUsername(String username){
		return usersRepository.findByUsername(username);
	}

	@Override
	public Page<Users> findAll(Specification<Users> spec, Pageable pageable) {
		return usersRepository.findAll(spec, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.SpecificationFactory#buildSpecification(fr.wati.yacramanager.utils.Filter)
	 */
	@Override
	public Specification<Employe> buildSpecification(Filter filter) {
		if(filter!=null){
			FilterType filterType = filter.getType();
			switch (filterType) {
			case ARRAY:
				FilterArray filterArray=(FilterArray) filter;
				if("company".equals(filter.getField())){
					List<Company> companies=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						companies.add(companyService.findOne(Long.valueOf(filterArrayValue.getName())));
					}
					return EmployeSpecifications.forCompanies(companies);
				}
				if("civilite".equals(filter.getField())){
					List<Civilite> civilities=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						civilities.add(Civilite.valueOf(filterArrayValue.getName()));
					}
					return EmployeSpecifications.withCivilities(civilities);
				}
				break;
			case TEXT:
				FilterText filterText=(FilterText) filter;
				if("nom".equals(filterText.getField())){
					return EmployeSpecifications.lastNamelike(filterText.getValue());
				}
				if("prenom".equals(filterText.getField())){
					return EmployeSpecifications.firstNamelike(filterText.getValue());
				}
				break;
			case DATE:
				FilterDate filterDate=(FilterDate) filter;
				if("dateNaissance".equals(filter.getField())){
					return EmployeSpecifications.birthDayBetween(filterDate.getValue().getStart(), filterDate.getValue().getEnd());
				}
				break;
			default:
				break;
			}
		}
		return null;
	}
	
	
}

