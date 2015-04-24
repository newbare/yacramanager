package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
import fr.wati.yacramanager.beans.CompanyTempInvitation;
import fr.wati.yacramanager.beans.Contact;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Gender;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.dao.JdbcCompanyInvitationRepository;
import fr.wati.yacramanager.dao.repository.CompanyAccountInfoRepository;
import fr.wati.yacramanager.dao.repository.ContactRepository;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.dao.repository.EmployeRepository;
import fr.wati.yacramanager.dao.repository.RoleRepository;
import fr.wati.yacramanager.dao.specifications.EmployeSpecifications;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.MailService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.TaskService;
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

	@Inject
	private EmployeRepository employeRepository;
	
	@Inject
	private ContactRepository contactRepository;

	@Inject
	private DozerBeanMapperFactoryBean dozerBeanMapper;
	
	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private CompanyService companyService;
	
	@Inject
	private ProjectService  projectService;
	
	@Inject
	private RoleRepository roleRepository;
	
	@Inject
	private TaskService  taskService;
	
	@Inject
	private MailService mailService;
	
	@Inject
	private CompanyAccountInfoRepository companyAccountInfoRepository;
	
	@Inject
	private Environment environment;
	
	@Inject
	private JdbcCompanyInvitationRepository companyInvitationRepository;

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
		delete(employeRepository.findOne(id));
	}

	@Override
	@Transactional
	public void delete(Employe entity) {
		for (Project project: entity.getProjects()) {
			if(project.getAssignedEmployees().contains(entity)){
				project.getAssignedEmployees().remove(entity);
				projectService.save(project);
			}
		}
		entity.getProjects().clear();
		Employe savedEmploye = employeRepository.save(entity);
		employeRepository.delete(savedEmploye);
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
		return employeRepository.findByEmail(username);
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
	public Employe registerEmploye(RegistrationDTO registrationDTO,boolean isSocialRegistration) throws ServiceException{
		Employe employe=new Employe();
		employe.setUserName(registrationDTO.getEmail());
		if(isSocialRegistration){
			employe.setEnabled(true);
			employe.setSocialUser(true);
			employe.setSocialUserId(registrationDTO.getSocialUserId());
			employe.setSocialProviderId(registrationDTO.getSocialProviderId());
		}else {
			employe.setActivationKey(RandomUtil.generateActivationKey());
		}
		employe.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
		employe.setLastName(registrationDTO.getLastName());
		employe.setFirstName(registrationDTO.getFirstName());
		employe.setGender(registrationDTO.getGender());
		employe.setProfileImageUrl(registrationDTO.getProfileImageUrl());
		employe.setProfileUrl(registrationDTO.getProfileUrl());
		Contact contact=new Contact();
		contact.setEmail(registrationDTO.getEmail());
		contactRepository.save(contact);
		employe.setContact(contact);
		Company company=new Company();
		company.setName(registrationDTO.getCompanyName());
		company.setRegisteredDate(new DateTime());
		if(registrationDTO.getCompanyInvitation()!=null){
			//check invitation validity
			CompanyTempInvitation givenInvitation = registrationDTO.getCompanyInvitation();
			CompanyTempInvitation invitation = companyInvitationRepository.findInvitationWithToken(givenInvitation.getUserId(), givenInvitation.getCompanyId(), givenInvitation.getToken());
			if(invitation!=null && invitation.isStillValid()){
				Company company2 = companyService.findOne(Long.valueOf(invitation.getCompanyId()));
				employe.setCompany(company2);
			}else {
				throw new ServiceException("The given invitation is not valid");
			}
			
		}else {
			//initialize company
			Company createCompany = companyService.createCompany(company);
			employe.setCompany(createCompany);
			CompanyAccountInfo companyAccountInfo=new CompanyAccountInfo();
			companyAccountInfo.setLocked(false);
			companyAccountInfo.setExpiredDate(new LocalDate().plusDays(environment.getProperty("yacra.trial.period.days", Integer.class, 30)));
			companyAccountInfo.setCompany(createCompany);
			CompanyAccountInfo savedCompanyAccountInfo = companyAccountInfoRepository.save(companyAccountInfo);
			createCompany.setCompanyAccountInfo(savedCompanyAccountInfo);
			createCompany.getClients().get(0).getProjects().get(0).getAssignedEmployees().add(employe);
			employe.getProjects().add(createCompany.getClients().get(0).getProjects().get(0));
			createCompany.getClients().get(0).getProjects().get(0).getTasks().get(0).getAssignedEmployees().add(employe);
			employe.getTasks().add(createCompany.getClients().get(0).getProjects().get(0).getTasks().get(0));
		}
		
		Set<Role> roles=new HashSet<>();
		roles.add(roleRepository.findByRole(Role.SSII_ADMIN));
		roles.add(roleRepository.findByRole(Role.INDEP));
		employe.setRoles(roles);
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
				if("projects".equals(filter.getField())){
					List<Project> projects=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						projects.add(projectService.findOne(Long.valueOf(filterArrayValue.getName())));
					}
					return EmployeSpecifications.forProjects(projects);
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
		employe.setUserName(employeDto.getEmail());
		employe.setPassword(employeDto.getPassword());
		employe.setGender(employeDto.getGender());
		employe.setBirthDay(employeDto.getBirthDay());
		Contact contact=new Contact();
		contact.setEmail(employeDto.getEmail());
		employe.setContact(contact);
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

	@Override
	public List<Employe> getEmployeesAssignedToTask(Long requesterId,
			Long taskId) {
		Task findOneTask = taskService.findOne(taskId);
		if(findOneTask==null){
			throw new IllegalArgumentException("No task found for id: "+taskId);
		}
		return employeRepository.findByTasksIn(findOneTask);
	}

	
	@Override
	@Transactional
	public void updateManager(Long employeeId, Long managerId) throws ServiceException {
		Employe employee = employeRepository.findOne(employeeId);
		Employe employeeManager = employeRepository.findOne(managerId);
		if(employee==null){
			throw new ServiceException("The given employee Id doesn't exist");
		}
		if(employeeManager==null){
			throw new ServiceException("The given employee manager Id doesn't exist");
		}
		employee.setManager(employeeManager);
		if(!employeeManager.getManagedEmployes().contains(employee)){
			employeeManager.getManagedEmployes().add(employee);
		}
		
	}

}
