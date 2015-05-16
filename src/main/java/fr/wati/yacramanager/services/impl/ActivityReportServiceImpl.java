package fr.wati.yacramanager.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.ActivityReport;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.dao.repository.ActivityReportRepository;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.ActivityReportService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;

@Service("activityReportService")
@Transactional
public class ActivityReportServiceImpl implements ActivityReportService {
	
	private Logger logger = LoggerFactory
			.getLogger(ActivityReportServiceImpl.class);
	
	@Inject
	private ActivityReportRepository activityReportRepository;
	
	@Inject
	private EmployeService employeService;
	private ApplicationEventPublisher applicationEventPublisher;

	public ActivityReportServiceImpl() {
	}

	@Override
	public void submitNewActivityReport(Employe employe, LocalDate startDate,
			LocalDate endDate) throws ServiceException {
		ActivityReport activityReport=null;
		ActivityReport existingActivityReport = activityReportRepository.findByEmployeIdAndStartDateAndEndDate(employe.getId(), startDate, endDate);
		if(existingActivityReport!=null){
			activityReport=existingActivityReport;
		}else {
			activityReport=new ActivityReport();
			activityReport.setEmployeId(employe.getId());
//			activityReport.setEmploye(employe);
			activityReport.setStartDate(startDate);
			activityReport.setEndDate(endDate);
		}
		activityReport.setValidationStatus(ValidationStatus.WAIT_FOR_APPROVEMENT);
		activityReportRepository.save(activityReport);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(ActivityOperation.CREATE)
				.onEntity(ActivityReport.class, 0L).dto(activityReport));
	}

	@Override
	public List<ActivityReport> findByEmployeAndStartDateBetweenAndEndDateBetween(
			Employe employe, LocalDate startDate, LocalDate endDate)
			throws ServiceException {
		return activityReportRepository.findByEmployeIdAndStartDateBetweenAndEndDateBetween(employe.getId(), startDate, endDate,startDate,endDate);
	}

	@Override
	public <S extends ActivityReport> S save(S entity) {
		return activityReportRepository.save(entity);
	}

	@Override
	public <S extends ActivityReport> Iterable<S> save(Iterable<S> entities) {
		return activityReportRepository.save(entities);
	}

	@Override
	public ActivityReport findOne(Long id) {
		return activityReportRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return activityReportRepository.exists(id);
	}

	@Override
	public Iterable<ActivityReport> findAll() {
		return activityReportRepository.findAll();
	}

	@Override
	public Iterable<ActivityReport> findAll(Iterable<Long> ids) {
		return activityReportRepository.findAll(ids);
	}

	@Override
	public long count() {
		return activityReportRepository.count();
	}

	@Override
	public void delete(Long id) {
		activityReportRepository.delete(id);
	}

	@Override
	public void delete(ActivityReport entity) {
		activityReportRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends ActivityReport> entities) {
		activityReportRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		activityReportRepository.deleteAll();
	}

	@Override
	public Page<ActivityReport> findAll(Specification<ActivityReport> spec,
			Pageable pageable) {
		return activityReportRepository.findAll(spec, pageable);
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	@Override
	public void cancelSubmittedActivityReport(Employe employe,LocalDate startDate,LocalDate endDate) throws ServiceException {
		ActivityReport activityReport = activityReportRepository.findByEmployeIdAndStartDateAndEndDate(employe.getId(), startDate, endDate);
		if(activityReport==null){
			throw new ServiceException("Activity report not found");
		}
		if(employeService.isManager(employe.getId(), activityReport.getEmployeId()) || employe.getId().equals(activityReport.getEmployeId())){
			if(ValidationStatus.WAIT_FOR_APPROVEMENT.equals(activityReport.getValidationStatus())){
				logger.debug("Delete submitted activity report as requested {}",activityReport);
				activityReportRepository.delete(activityReport);
				applicationEventPublisher.publishEvent(ActivityEvent
						.createWithSource(this).user()
						.operation(ActivityOperation.DELETE)
						.onEntity(ActivityReport.class, 0L).dto(activityReport));
			}else {
				throw new ServiceException("You cannot cancel this activity report");
			}
			
		}else {
			throw new ServiceException("You cannot cancel this activity report");
		}
		
	}

	@Override
	public void approveSubmittedActivityReport(Employe employeManager,ActivityReport activityReport)
			throws ServiceException {
		if(activityReport!=null && !ValidationStatus.WAIT_FOR_APPROVEMENT.equals(activityReport.getValidationStatus())){
			throw new ServiceException("The activity report should be in a right status");
		}
		if(employeService.isManager(employeManager.getId(), activityReport.getEmployeId())){
			if(ValidationStatus.WAIT_FOR_APPROVEMENT.equals(activityReport.getValidationStatus())){
				activityReport.setValidationStatus(ValidationStatus.APPROVED);
				activityReportRepository.save(activityReport);
				applicationEventPublisher.publishEvent(ActivityEvent
						.createWithSource(this).user()
						.operation(ActivityOperation.VALIDATE)
						.onEntity(ActivityReport.class, 0L).dto(activityReport));
			}else {
				throw new ServiceException("You cannot approve this activity report due to its status: "+activityReport.getValidationStatus());
			}
		}else {
			throw new ServiceException("Only a manager can approve this activity report");
		}
		
	}

	@Override
	public ActivityReport findByEmployeAndStartDateAndEndDate(Employe employe,
			LocalDate startDate, LocalDate endDate) {
		return activityReportRepository.findByEmployeIdAndStartDateAndEndDate(employe.getId(), startDate, endDate);
	}

	@Override
	public List<ActivityReport> findByEmployeInAndValidationStatusIn(
			List<Long> employeIds, List<ValidationStatus> validationStatus) {
		return activityReportRepository.findByEmployeIdInAndValidationStatusIn(employeIds, validationStatus);
	}

	@Override
	public void rejectSubmittedActivityReport(Employe employeManager,
			ActivityReport activityReport) throws ServiceException {
		if(activityReport!=null && !ValidationStatus.WAIT_FOR_APPROVEMENT.equals(activityReport.getValidationStatus())){
			throw new ServiceException("The activity report should be in a right status");
		}
		if(employeService.isManager(employeManager.getId(), activityReport.getEmployeId())){
			if(ValidationStatus.WAIT_FOR_APPROVEMENT.equals(activityReport.getValidationStatus())){
				activityReport.setValidationStatus(ValidationStatus.REJECTED);
				activityReportRepository.save(activityReport);
				applicationEventPublisher.publishEvent(ActivityEvent
						.createWithSource(this).user()
						.operation(ActivityOperation.REJECT)
						.onEntity(ActivityReport.class, 0L).dto(activityReport));
			}else {
				throw new ServiceException("You cannot reject this activity report due to its status: "+activityReport.getValidationStatus());
			}
		}else {
			throw new ServiceException("Only a manager can reject this activity report");
		}
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.ActivityReportService#findByEmployeIdAndStartDateBetweenAndEndDateBetweenAndValidationStatusIn(java.lang.Long, org.joda.time.LocalDate, org.joda.time.LocalDate, org.joda.time.LocalDate, org.joda.time.LocalDate, java.util.List)
	 */
	@Override
	public List<ActivityReport> findApprovedBetweenDate(
			Long employeId, LocalDate startDate, LocalDate endDate,	List<ValidationStatus> validationStatus) {
		return activityReportRepository.findApprovedBetweenDate(employeId, startDate, endDate, validationStatus);
	}

}
