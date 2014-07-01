package fr.wati.yacramanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.dao.PersonRepository;

@Transactional
@Service
public class PersonService implements CrudService<Personne, Long>{

	@Autowired
	private PersonRepository personRepository;
	
	public PersonService() {
	}

	@Override
	public <S extends Personne> S save(S entity) {
		return personRepository.save(entity);
	}

	@Override
	public <S extends Personne> Iterable<S> save(Iterable<S> entities) {
		return personRepository.save(entities);
	}

	@Override
	public Personne findOne(Long id) {
		return personRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return personRepository.exists(id);
	}

	@Override
	public Iterable<Personne> findAll() {
		return personRepository.findAll();
	}

	@Override
	public Iterable<Personne> findAll(Iterable<Long> ids) {
		return personRepository.findAll(ids);
	}

	@Override
	public long count() {
		return personRepository.count();
	}

	@Override
	public void delete(Long id) {
		personRepository.delete(id);
	}

	@Override
	public void delete(Personne entity) {
		personRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Personne> entities) {
		personRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		personRepository.deleteAll();
	}

	@Transactional(readOnly=true)
	public Personne findByUsername(String username){
		return personRepository.findByUsername(username);
	}
	
}

