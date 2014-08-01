package fr.wati.yacramanager.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.dao.repository.WorkLogRepository;
import fr.wati.yacramanager.services.WorkLogService;
import fr.wati.yacramanager.utils.Filter;

@Service
@Transactional
public class WorkLogServiceImpl implements WorkLogService {

	@Autowired
	private WorkLogRepository workLogRepository;
	@Override
	public <S extends WorkLog> S save(S entity) {
		return workLogRepository.save(entity);
	}

	@Override
	public <S extends WorkLog> Iterable<S> save(Iterable<S> entities) {
		return workLogRepository.save(entities);
	}

	@Override
	public WorkLog findOne(Long id) {
		return workLogRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return workLogRepository.exists(id);
	}

	@Override
	public Iterable<WorkLog> findAll() {
		return workLogRepository.findAll();
	}

	@Override
	public Iterable<WorkLog> findAll(Iterable<Long> ids) {
		return workLogRepository.findAll(ids);
	}

	@Override
	public long count() {
		return workLogRepository.count();
	}

	@Override
	public void delete(Long id) {
		workLogRepository.delete(id);
	}

	@Override
	public void delete(WorkLog entity) {
		workLogRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends WorkLog> entities) {
		workLogRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		workLogRepository.deleteAll();
	}

	@Override
	public Page<WorkLog> findAll(Specification<WorkLog> spec, Pageable pageable) {
		return workLogRepository.findAll(spec, pageable);
	}

	@Override
	public Specification<WorkLog> buildSpecification(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<WorkLog> findByStartDateBetween(Date dateDebut, Date dateFin,
			Pageable pageable) {
		return workLogRepository.findByStartDateBetween(dateDebut, dateFin, pageable);
	}

	@Override
	public Page<WorkLog> findByEmployeAndStartDateBetween(Employe employe,
			Date dateDebut, Date dateFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkLog> findByEmployeAndStartDateBetween(Employe employe,
			Date dateDebut, Date dateFin) {
		return workLogRepository.findByEmployeAndStartDateBetween(employe, dateDebut, dateFin);
	}

	@Override
	public Page<WorkLog> findByEmploye(Employe employe, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
