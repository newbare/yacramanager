package fr.wati.yacramanager.services.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Contact;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Role;
import fr.wati.yacramanager.dao.repository.EmployeRepository;
import fr.wati.yacramanager.dao.repository.RoleRepository;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.web.dto.RegistrationDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

@Transactional
@Service
public class EmployeServiceImpl implements EmployeService {

	@Autowired
	private EmployeRepository employeRepository;

	@Autowired
	private DozerBeanMapperFactoryBean dozerBeanMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private RoleRepository roleRepository;

	public EmployeServiceImpl() {
	}

	@Override
	public <S extends Employe> S save(S entity) {
		return employeRepository.save(entity);
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
		employe.setEnabled(true);
		employe.setNom(registrationDTO.getLastName());
		employe.setPrenom(registrationDTO.getFirstName());
		Contact contact=new Contact();
		contact.setEmail(registrationDTO.getEmail());
		employe.setContact(contact);
		Company company=new Company();
		company.setName(registrationDTO.getCompanyName());
		company.setRegisteredDate(new Date());
		Company createCompany = companyService.createCompany(company);
		employe.setCompany(createCompany);
		Set<Role> roles=new HashSet<>();
		roles.add(roleRepository.findByRole(Role.ROLE_SSII_ADMIN));
		roles.add(roleRepository.findByRole(Role.ROLE_INDEP));
		employe.setRoles(roles);
		Employe saveEmploye = employeRepository.save(employe);
		return saveEmploye;
	}
}
