package fr.wati.yacramanager.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.dao.UsersRepository;
import fr.wati.yacramanager.services.CrudService;
import fr.wati.yacramanager.services.UserService;

@Transactional
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UsersRepository usersRepository;
	
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

	
	
}

