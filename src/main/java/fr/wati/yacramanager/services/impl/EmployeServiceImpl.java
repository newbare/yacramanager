package fr.wati.yacramanager.services.impl;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.dao.EmployeRepository;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

@Transactional
@Service
public class EmployeServiceImpl implements EmployeService {

	@Autowired
	private EmployeRepository employeRepository;

	@Autowired
	private DozerBeanMapperFactoryBean dozerBeanMapper;

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
}
