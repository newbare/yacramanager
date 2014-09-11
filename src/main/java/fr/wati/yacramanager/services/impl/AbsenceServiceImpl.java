package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.AbsencePortfolio;
import fr.wati.yacramanager.beans.AbsencePortfolio.AbsencePortfolioPK;
import fr.wati.yacramanager.beans.Absence_;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.dao.repository.AbsenceRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.AbsencePortfolioService;
import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterDate;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

@Service
public class AbsenceServiceImpl implements AbsenceService {

	@Autowired
	private AbsenceRepository absenceRepository;
	
	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private AbsencePortfolioService absencePortfolioService;

	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public Page<Absence> findByStartDateBetween(DateTime dateDebut, DateTime dateFin,
			Pageable pageable) {
		return absenceRepository.findByDateBetween(dateDebut, dateFin, pageable);
	}

	@Override
	public <S extends Absence> S save(S entity){
		S save = absenceRepository.save(entity);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(ActivityOperation.CREATE)
				.onEntity(Absence.class, save.getId()));
		return save;
	}


	@Override
	public <S extends Absence> Iterable<S> save(Iterable<S> entities) {
		return absenceRepository.save(entities);
	}


	@Override
	public Absence findOne(Long id) {
		return absenceRepository.findOne(id);
	}


	@Override
	public boolean exists(Long id) {
		return absenceRepository.exists(id);
	}


	@Override
	public Iterable<Absence> findAll() {
		return absenceRepository.findAll();
	}


	@Override
	public Iterable<Absence> findAll(Iterable<Long> ids) {
		return absenceRepository.findAll(ids);
	}


	@Override
	public long count() {
		return absenceRepository.count();
	}


	@Override
	public void delete(Long id) {
		absenceRepository.delete(id);
	}


	@Override
	public void delete(Absence entity) {
		absenceRepository.delete(entity);
	}


	@Override
	public void delete(Iterable<? extends Absence> entities) {
		absenceRepository.delete(entities);
	}


	@Override
	public void deleteAll() {
		absenceRepository.deleteAll();
	}


	@Override
	public Page<Absence> findByEmployeAndStartDateBetween(Employe employe,
			DateTime dateDebut, DateTime dateFin, Pageable pageable) {
		return absenceRepository.findByEmployeAndStartDateBetween(employe, dateDebut, dateFin, pageable);
	}


	@Override
	public Page<Absence> findByEmploye(Employe employe, Pageable pageable) {
		return absenceRepository.findByEmploye(employe, pageable);
	}


	@Override
	public List<Absence> findByEmployeAndStartDateBetween(Employe employe,
			DateTime dateDebut, DateTime dateFin) {
		return absenceRepository.findByEmployeAndStartDateBetween(employe, dateDebut, dateFin);
	}


	@Override
	public Page<Absence> findAll(Specification<Absence> spec, Pageable pageable) {
		return absenceRepository.findAll(spec, pageable);
	}


	@Override
	public Specification<Absence> buildSpecification(Filter filter) {
		if(filter!=null){
			FilterType filterType = filter.getType();
			switch (filterType) {
			case ARRAY:
				FilterArray filterArray=(FilterArray) filter;
				if("type".equals(filter.getField())){
					List<TypeAbsence> typeabsences=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						typeabsences.add(TypeAbsence.valueOf(filterArrayValue.getName()));
					}
					return CommonSpecifications.equalsAny(typeabsences, Absence_.typeAbsence);
				}
				if("employe".equals(filter.getField())){
					List<Employe> employes=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						employes.add(employeService.findOne(Long.valueOf(filterArrayValue.getName())));
					}
					return CommonSpecifications.equalsAny(employes, Absence_.employe);
				}
				break;
			case TEXT:
				FilterText filterText=(FilterText) filter;
				if("description".equals(filterText.getField())){
					return CommonSpecifications.likeIgnoreCase(filterText.getValue(), Absence_.description);
				}
				break;
			case DATE:
			case DATE_RANGE:
				FilterDate filterDate=(FilterDate) filter;
				if("date".equals(filter.getField())){
					if(filterDate.isRangedDate()){
						return CommonSpecifications.betweenDate(filterDate.getValue().getStart(), filterDate.getValue().getEnd(),Absence.class, "date");
					}else {
						return CommonSpecifications.equals(filterDate.getValue().getDate(), Absence_.date);
					}
				}
				break;
			default:
				break;
			}
		}
		return null;
	}


	@Override
	public void validate(Employe validator, Absence absence) throws ServiceException{
		Absence findOne = absenceRepository.findOne(absence.getId());
		if(employeService.isManager(validator.getId(), findOne.getEmploye().getId())){
			findOne.setValidationStatus(ValidationStatus.APPROVED);
			Absence save = absenceRepository.save(findOne);
			AbsencePortfolio absencePortfolio = absencePortfolioService.findByUserAndType(save.getEmploye().getId(), save.getTypeAbsence());
			absencePortfolio.incrementConsumed(absence.getDaysBetween());
			absencePortfolio.incrementRemaining(-absence.getDaysBetween());
			absencePortfolio.incrementWaiting(-absence.getDaysBetween());
			absencePortfolioService.save(absencePortfolio);
			applicationEventPublisher.publishEvent(ActivityEvent
					.createWithSource(this).user(validator)
					.operation(ActivityOperation.REJECT)
					.onEntity(Absence.class, save.getId()));
		}else {
			throw new ServiceException(validator.getFullName()+ " is not the manager of "+findOne.getEmploye().getFullName());
		}
	}


	@Override
	public void reject(Employe validator, Absence absence) throws ServiceException {
		Absence findOne = absenceRepository.findOne(absence.getId());
		if(employeService.isManager(validator.getId(), findOne.getEmploye().getId())){
			findOne.setValidationStatus(ValidationStatus.REJECTED);
			Absence save = absenceRepository.save(findOne);
			applicationEventPublisher.publishEvent(ActivityEvent
					.createWithSource(this).user(validator)
					.operation(ActivityOperation.REJECT)
					.onEntity(Absence.class, save.getId()));
		}else {
			throw new ServiceException(validator.getFullName()+ " is not the manager of "+findOne.getEmploye().getFullName());
		}
	}
	
	public List<Absence> getEntitiesToApproved(Long employeId){
		List<Employe> managedEmployes=employeService.getManagedEmployees(employeId);
		List<Absence> absencesToApproved=new ArrayList<>();
		for (Employe employe : managedEmployes) {
			Specifications<Absence> specifications = Specifications.where(CommonSpecifications.equals(employe, Absence_.employe))
			.and(CommonSpecifications.equalsAny(Lists.newArrayList(ValidationStatus.WAIT_FOR_APPROVEMENT), Absence_.validationStatus));
			absencesToApproved.addAll(absenceRepository.findAll(specifications));
		}
		return absencesToApproved;
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	@Override
	public Absence postAbsence(Long employeId, Absence absence)
			throws ServiceException {
		Employe employe = employeService.findOne(employeId);
		TypeAbsence typeAbsence = absence.getTypeAbsence();
		AbsencePortfolio absencePortfolio = absencePortfolioService.findByUserAndType(employe.getId(), typeAbsence);
		if(absencePortfolio==null){
			throw new ServiceException("No absence portfolio found for user "+employe.getId());
		}
		if((absence.getDaysBetween()+absencePortfolio.getWaiting())>absencePortfolio.getRemaining()){
			throw new ServiceException("You do not have enought remainnig days for "+typeAbsence.getLabel()+" Remaining: "+absencePortfolio.getRemaining()+" Waiting: "+absencePortfolio.getWaiting());
		}
		absencePortfolio.incrementWaiting(absence.getDaysBetween());
		absencePortfolioService.save(absencePortfolio);
		return save(absence);
	}
	
	@Scheduled(cron="0 0/15 * * * *")
	@Override
	public void periodicalyIncrementAbsence() {
		Iterable<Employe> employes=employeService.findAll();
		for (Employe employe : employes) {
			for(TypeAbsence typeAbsence:TypeAbsence.values()){
				AbsencePortfolio absencePortfolio = absencePortfolioService.findByUserAndType(employe.getId(), typeAbsence);
				if(absencePortfolio==null){
					absencePortfolio=new AbsencePortfolio();
					absencePortfolio.setAbsencePortfolioPK(new AbsencePortfolioPK(employe.getId(), typeAbsence));
					absencePortfolioService.save(absencePortfolio);
				}
				absencePortfolio.incrementRemaining(1);
				absencePortfolioService.save(absencePortfolio);
			}
		}
	}

}
