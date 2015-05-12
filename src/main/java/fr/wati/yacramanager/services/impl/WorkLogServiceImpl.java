package fr.wati.yacramanager.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.dao.repository.WorkLogRepository;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.services.SpecificationFactory;
import fr.wati.yacramanager.services.WorkLogService;
import fr.wati.yacramanager.utils.Filter;

@Service
@Transactional
public class WorkLogServiceImpl implements WorkLogService,SpecificationFactory<WorkLog> {

	@Inject
	private WorkLogRepository workLogRepository;
	
	@Inject
	private EmployeService employeService;

	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public <S extends WorkLog> S save(S entity) {
		ActivityOperation activityOperation=entity.getId()==null?ActivityOperation.CREATE:ActivityOperation.UPDATE;
		S save = workLogRepository.save(entity);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(activityOperation)
				.onEntity(WorkLog.class, save.getId()));
		return save;
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
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(ActivityOperation.DELETE)
				.onEntity(WorkLog.class, id));
	}

	@Override
	public void delete(WorkLog entity) {
		delete(entity.getId());
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
	public Page<WorkLog> findByStartDateBetween(LocalDateTime dateDebut, LocalDateTime dateFin,
			Pageable pageable) {
		return workLogRepository.findByStartDateBetween(dateDebut, dateFin, pageable);
	}

	@Override
	public Page<WorkLog> findByEmployeAndStartDateBetween(Employe employe,
			LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorkLog> findByEmployeAndStartDateBetween(Employe employe,
			LocalDateTime dateDebut, LocalDateTime dateFin) {
		return workLogRepository.findByEmployeAndStartDateBetween(employe, dateDebut, dateFin);
	}

	@Override
	public Page<WorkLog> findByEmploye(Employe employe, Pageable pageable) {
		return workLogRepository.findByEmploye(employe, pageable);
	}

	@Override
	public void validate(Employe validator, WorkLog workLog) throws ServiceException {
		WorkLog findOne = workLogRepository.findOne(workLog.getId());
		if(employeService.isManager(validator.getId(), findOne.getEmploye().getId())){
			findOne.setValidationStatus(ValidationStatus.APPROVED);
			workLogRepository.save(findOne);
			applicationEventPublisher.publishEvent(ActivityEvent
					.createWithSource(this).user()
					.operation(ActivityOperation.VALIDATE)
					.onEntity(WorkLog.class, findOne.getId()));
		}else {
			throw new ServiceException(validator.getFullName()+ " is not the manager of "+findOne.getEmploye().getFullName());
		}
	}

	@Override
	public void reject(Employe validator, WorkLog workLog) throws ServiceException {
		WorkLog findOne = workLogRepository.findOne(workLog.getId());
		if(employeService.isManager(validator.getId(), findOne.getEmploye().getId())){
			findOne.setValidationStatus(ValidationStatus.REJECTED);
			workLogRepository.save(findOne);
			applicationEventPublisher.publishEvent(ActivityEvent
					.createWithSource(this).user()
					.operation(ActivityOperation.REJECT)
					.onEntity(WorkLog.class, findOne.getId()));
		}else {
			throw new ServiceException(validator.getFullName()+ " is not the manager of "+findOne.getEmploye().getFullName());
		}
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.StatusValidator#getEntitiesToApproved(java.lang.Long)
	 */
	@Override
	public List<WorkLog> getEntitiesToApproved(Long employeId) {
		return null;
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	@Override
	public Page<WorkLog> findExtraTime(Employe employe, LocalDateTime dateDebut,
			LocalDateTime dateFin, Pageable pageable) {
		return workLogRepository.findByEmployeAndStartDateBetweenAndExtraTimeTrue(employe, dateDebut, dateFin, pageable);
	}

	@Override
	public List<WorkLog> findExtraTime(Employe employe, LocalDateTime dateDebut,
			LocalDateTime dateFin) {
		return workLogRepository.findByEmployeAndStartDateBetweenAndExtraTimeTrue(employe, dateDebut, dateFin);
	}

	@Override
	public List<WorkLog> findByEmployeAndStartDateBetweenAndExtratimeFalse(
			Employe employe, LocalDateTime dateDebut, LocalDateTime dateFin) {
		// TODO Auto-generated method stub
		return workLogRepository.findByEmployeAndStartDateBetweenAndExtraTimeFalse(employe, dateDebut, dateFin);
	}
}
