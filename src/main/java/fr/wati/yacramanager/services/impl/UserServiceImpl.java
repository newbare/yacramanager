package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Gender;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.dao.repository.UserRepository;
import fr.wati.yacramanager.dao.specifications.EmployeSpecifications;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterDate;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService{

	private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private CompanyService companyService;

	private ApplicationEventPublisher applicationEventPublisher;
	
	public UserServiceImpl() {
	}

	@Override
	public <S extends Users> S save(S entity) {
		return userRepository.save(entity);
	}

	@Override
	public <S extends Users> Iterable<S> save(Iterable<S> entities) {
		return userRepository.save(entities);
	}

	@Override
	@Transactional(readOnly=true)
	public Users findOne(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean exists(Long id) {
		return userRepository.exists(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<Users> findAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<Users> findAll(Iterable<Long> ids) {
		return userRepository.findAll(ids);
	}

	@Override
	@Transactional(readOnly=true)
	public long count() {
		return userRepository.count();
	}

	@Override
	public void delete(Long id) {
		userRepository.delete(id);
	}

	@Override
	public void delete(Users entity) {
		userRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Users> entities) {
		userRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	public Users findByEmail(String email){
		return userRepository.findByEmail(email);
	}

	@Override
	public Page<Users> findAll(Specification<Users> spec, Pageable pageable) {
		return userRepository.findAll(spec, pageable);
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
				if("nom".equals(filterText.getField())){
					return EmployeSpecifications.lastNamelike(filterText.getValue());
				}
				if("prenom".equals(filterText.getField())){
					return EmployeSpecifications.firstNamelike(filterText.getValue());
				}
				break;
			case DATE:
			case DATE_RANGE:
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
	
	public void changePassword(Long userId,String password) {
        Users currentUser = userRepository.findOne(userId);
        String encryptedPassword = passwordEncoder.encode(password);
        currentUser.setPassword(encryptedPassword);
        userRepository.save(currentUser);
        logger.debug("Changed password for User: {}", currentUser.getUserName());
    }
	
	public Users activateRegistration(String key) {
        logger.debug("Activating user for activation key {}", key);
        Users user = userRepository.getUserByActivationKey(key);

        // activate given user for the registration key.
        if (user != null) {
            user.setEnabled(true);
            user.setActivationKey(null);
            userRepository.save(user);
            logger.debug("Activated user: {}", user);
        }
        return user;
    }
	
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.UserService#sendActivationMail(java.lang.Long)
	 */
	@Override
	public void sendActivationMail(Long userId) throws ServiceException {
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.UserService#findBySocialUserIdAndSocialProviderId(java.lang.String, java.lang.String)
	 */
	@Override
	public Users findBySocialUserIdAndSocialProviderId(String socialUserId,
			String socialProviderId) {
		return userRepository.findBySocialUserIdAndSocialProviderId(socialUserId, socialProviderId);
	}
	
}

