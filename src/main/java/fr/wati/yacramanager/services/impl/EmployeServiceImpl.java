package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.CompanyAccountInfo;
import fr.wati.yacramanager.beans.Contact;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Gender;
import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.dao.repository.CompanyAccountInfoRepository;
import fr.wati.yacramanager.dao.repository.ContactRepository;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.dao.repository.EmployeRepository;
import fr.wati.yacramanager.dao.repository.RoleRepository;
import fr.wati.yacramanager.dao.specifications.EmployeSpecifications;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterDate;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;
import fr.wati.yacramanager.utils.RandomUtil;
import fr.wati.yacramanager.web.dto.RegistrationDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

@Transactional
@Service
public class EmployeServiceImpl implements EmployeService {

	@Autowired
	private EmployeRepository employeRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private DozerBeanMapperFactoryBean dozerBeanMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CompanyAccountInfoRepository companyAccountInfoRepository;
	
	@Autowired
	private Environment environment;

	private ApplicationEventPublisher applicationEventPublisher;

	public EmployeServiceImpl() {
	}

	@Override
	public <S extends Employe> S save(S entity) {
		if(entity.getContact()!=null){
			contactRepository.save(entity.getContact());
		}
		S save = employeRepository.save(entity);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(ActivityOperation.CREATE)
				.onEntity(Employe.class, save.getId()));
		return save;
	}

	@Override
	public <S extends Employe> Iterable<S> save(Iterable<S> entities) {
		return employeRepository.save(entities);
	}

	@Override
	public Employe findOne(Long id) {
		return employeRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return employeRepository.exists(id);
	}

	@Override
	public Iterable<Employe> findAll() {
		return employeRepository.findAll();
	}

	@Override
	public Iterable<Employe> findAll(Iterable<Long> ids) {
		return employeRepository.findAll(ids);
	}

	@Override
	public long count() {
		return employeRepository.count();
	}

	@Override
	public void delete(Long id) {
		employeRepository.delete(id);
	}

	@Override
	public void delete(Employe entity) {
		employeRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Employe> entities) {
		employeRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		employeRepository.deleteAll();
	}

	@Transactional(readOnly = true)
	public Employe findByUsername(String username) {
		return employeRepository.findByUsername(username);
	}

	@Transactional
	public UserInfoDTO toUserInfoDTO(Long idPersonne) throws Exception {
		Employe loadEmploye = employeRepository.findOne(idPersonne);
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		((Mapper) dozerBeanMapper.getObject()).map(loadEmploye, userInfoDTO);
		userInfoDTO.setPassword(null);
		return userInfoDTO;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.EmployeService#registerEmploye(fr.wati.yacramanager.web.dto.RegistrationDTO)
	 */
	@Override
	public Employe registerEmploye(RegistrationDTO registrationDTO) {
		Employe employe=new Employe();
		employe.setUsername(registrationDTO.getUsername());
		employe.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
		employe.setEnabled(false);
		employe.setLastName(registrationDTO.getLastName());
		employe.setFirstName(registrationDTO.getFirstName());
		Contact contact=new Contact();
		contact.setEmail(registrationDTO.getEmail());
		contactRepository.save(contact);
		employe.setContact(contact);
		Company company=new Company();
		company.setName(registrationDTO.getCompanyName());
		company.setRegisteredDate(new DateTime());
		Company createCompany = companyService.createCompany(company);
		employe.setCompany(createCompany);
		CompanyAccountInfo companyAccountInfo=new CompanyAccountInfo();
		companyAccountInfo.setLocked(false);
		companyAccountInfo.setExpiredDate(new LocalDate().plusDays(environment.getProperty("yacra.trial.period.days", Integer.class, 30)));
		companyAccountInfoRepository.save(companyAccountInfo);
		Set<Role> roles=new HashSet<>();
		roles.add(roleRepository.findByRole(Role.ROLE_SSII_ADMIN));
		roles.add(roleRepository.findByRole(Role.ROLE_INDEP));
		employe.setRoles(roles);
		createCompany.getClients().get(0).getProjects().get(0).getAssignedEmployees().add(employe);
		employe.getProjects().add(createCompany.getClients().get(0).getProjects().get(0));
		createCompany.getClients().get(0).getProjects().get(0).getTasks().get(0).getAssignedEmployees().add(employe);
		employe.getTasks().add(createCompany.getClients().get(0).getProjects().get(0).getTasks().get(0));
		employe.setActivationKey(RandomUtil.generateActivationKey());
		Employe saveEmploye = employeRepository.save(employe);
		return saveEmploye;
	}

	@Override
	public Page<Employe> findAll(Specification<Employe> spec, Pageable pageable) {
		return employeRepository.findAll(spec, pageable);
	}

	@Override
	public List<Employe> getManagedEmployees(Long requesterId) {
		Employe requester = employeRepository.findOne(requesterId);
		Specifications<Employe> specifications = Specifications.where(
				EmployeSpecifications.hasManager(requester)).and(
				EmployeSpecifications.forCompany(requester.getCompany()));
		List<Employe> requesterManagedEmployees = employeRepository
				.findAll(specifications);
		List<Employe> managedEmployees = new ArrayList<>();
		for (Employe employe : requesterManagedEmployees) {
			managedEmployees.addAll(getManagedEmployees(employe.getId()));
		}
		requesterManagedEmployees.addAll(managedEmployees);
		return requesterManagedEmployees;
	}

	@Override
	public void addManagedEmploye(Long managerId, Long employeId) {
		Employe manager=employeRepository.findOne(managerId);
		Employe managed=employeRepository.findOne(employeId);
		managed.setManager(manager);
		manager.getManagedEmployes().add(managed);
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
					List<Gender> civilities=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						civilities.add(Gender.valueOf(filterArrayValue.getName()));
					}
					return EmployeSpecifications.withGenders(civilities);
				}
				break;
			case TEXT:
				FilterText filterText=(FilterText) filter;
				if("lastName".equals(filterText.getField())){
					return EmployeSpecifications.lastNamelike(filterText.getValue());
				}
				if("firstName".equals(filterText.getField())){
					return EmployeSpecifications.firstNamelike(filterText.getValue());
				}
				break;
			case DATE:
			case DATE_RANGE:
				FilterDate filterDate=(FilterDate) filter;
				if("birthDay".equals(filter.getField())){
					return EmployeSpecifications.birthDayBetween(filterDate.getValue().getStart(), filterDate.getValue().getEnd());
				}
				break;
			default:
				break;
			}
		}
		return null;
	}

	@Override
	public void createNewEmployee(EmployeDto employeDto, Long companyId,
			Long managerId) {
		if(!companyService.exists(companyId)){
			throw new IllegalArgumentException("The given company doesn't exist");
		}
		Company company=companyService.findOne(companyId);
		Employe employe = new Employe();
		employe.setFirstName(employeDto.getFirstName());
		employe.setLastName(employeDto.getLastName());
		employe.setUsername(getDefaultUsername(employeDto.getFirstName(), employeDto.getLastName()));
		employe.setPassword(employeDto.getPassword());
		employe.setGender(employeDto.getGender());
		employe.setBirthDay(employeDto.getBirthDay());
		employe.getContact().setEmail(employeDto.getEmail());
		employe.setCompany(company);
		
		save(employe);
		company.getEmployes().add(employe);
		
		if(managerId!=null && exists(managerId)){
			Employe manager=findOne(managerId);
			employe.setManager(manager);
			manager.getManagedEmployes().add(employe);
		}
		
	}
	
	private String getDefaultUsername(String firstName,String lastName){
		return firstName.toLowerCase().substring(0, 1)+lastName.toLowerCase();
	}

	@Override
	public boolean isManager(Long requester, Long employeId) {
		return getManagedEmployees(requester).contains(employeRepository.findOne(employeId));
	}
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.EmployeService#findByContact_Email(java.lang.String)
	 */
	@Override
	public Employe findByContact_Email(String email) {
		return employeRepository.findByContact_Email(email);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.EmployeService#resetPassword(fr.wati.yacramanager.beans.Employe)
	 */
	@Override
	public String resetPassword(Employe employe) {
		String generatedPassword=RandomUtil.generatePassword();
		employe.setPassword(passwordEncoder.encode(generatedPassword));
		save(employe);
		return generatedPassword;
	}

}
