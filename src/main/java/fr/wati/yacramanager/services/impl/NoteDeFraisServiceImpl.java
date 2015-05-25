package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.NoteDeFrais_;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.dao.repository.NoteDeFraisRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.NoteDeFraisService;
import fr.wati.yacramanager.services.ServiceException;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterComparator;
import fr.wati.yacramanager.utils.Filter.FilterDate;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;

@Service("noteDeFraisService")
public class NoteDeFraisServiceImpl implements NoteDeFraisService {

	@Inject
	private NoteDeFraisRepository noteDeFraisRepository;
	@Inject
	private EmployeService employeService;
	private ApplicationEventPublisher applicationEventPublisher;
	
	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#save(java.lang.Object)
	 */
	@Override
	public <S extends NoteDeFrais> S save(S entity) {
		ActivityOperation activityOperation=entity.getId()==null?ActivityOperation.CREATE:ActivityOperation.UPDATE;
		S save = noteDeFraisRepository.save(entity);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(activityOperation)
				.onEntity(NoteDeFrais.class, save.getId()));
		return save;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#save(java.lang.Iterable)
	 */
	@Override
	public <S extends NoteDeFrais> Iterable<S> save(Iterable<S> entities) {
		return noteDeFraisRepository.save(entities);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findOne(java.io.Serializable)
	 */
	@Override
	public NoteDeFrais findOne(Long id) {
		return noteDeFraisRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#exists(java.io.Serializable)
	 */
	@Override
	public boolean exists(Long id) {
		return noteDeFraisRepository.exists(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll()
	 */
	@Override
	public Iterable<NoteDeFrais> findAll() {
		return noteDeFraisRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll(java.lang.Iterable)
	 */
	@Override
	public Iterable<NoteDeFrais> findAll(Iterable<Long> ids) {
		return noteDeFraisRepository.findAll(ids);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#count()
	 */
	@Override
	public long count() {
		return noteDeFraisRepository.count();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Long id) {
		noteDeFraisRepository.delete(id);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(ActivityOperation.DELETE)
				.onEntity(NoteDeFrais.class, id));
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.lang.Object)
	 */
	@Override
	public void delete(NoteDeFrais entity) {
		delete(entity.getId());
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.lang.Iterable)
	 */
	@Override
	public void delete(Iterable<? extends NoteDeFrais> entities) {
		noteDeFraisRepository.delete(entities);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#deleteAll()
	 */
	@Override
	public void deleteAll() {
		noteDeFraisRepository.deleteAll();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.NoteDeFraisService#findByDateBetween(java.util.Date, java.util.Date, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<NoteDeFrais> findByDateBetween(DateTime dateDebut,
			DateTime dateFin, Pageable pageable) {
		return noteDeFraisRepository.findByDateBetween(dateDebut, dateFin, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.NoteDeFraisService#findByPersonneAndDateBetween(fr.wati.yacramanager.beans.Personne, java.util.Date, java.util.Date, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<NoteDeFrais> findByEmployeAndDateBetween(
			Employe employe, DateTime dateDebut, DateTime dateFin, Pageable pageable) {
		return noteDeFraisRepository.findByEmployeAndDateBetween(employe, dateDebut, dateFin, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.NoteDeFraisService#findByPersonneAndDateBetween(fr.wati.yacramanager.beans.Personne, java.util.Date, java.util.Date)
	 */
	@Override
	public List<NoteDeFrais> findByEmployeAndDateBetween(
			Employe employe, DateTime dateDebut, DateTime dateFin) {
		return noteDeFraisRepository.findByEmployeAndDateBetween(employe, dateDebut, dateFin);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.NoteDeFraisService#findByPersonne(fr.wati.yacramanager.beans.Personne, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<NoteDeFrais> findByEmploye(Employe employe, Pageable pageable) {
		return noteDeFraisRepository.findByEmploye(employe, pageable);
	}

	@Transactional
	public List<NoteDeFraisDTO> mapNoteDeFrais(Iterable<NoteDeFrais> noteDeFrais) {
		List<NoteDeFraisDTO> dtos = new ArrayList<NoteDeFraisDTO>();
		for (NoteDeFrais noteDeFrai : noteDeFrais) {
			dtos.add(map(noteDeFrai));
		}
		return dtos;
	}
	
	@Transactional
	public NoteDeFraisDTO map(NoteDeFrais noteDeFrais) {
		NoteDeFrais findOne = noteDeFraisRepository.findOne(noteDeFrais.getId());
		NoteDeFraisDTO dto = new NoteDeFraisDTO();
		dto.setDate(findOne.getDate());
		dto.setDescription(findOne.getDescription());
		dto.setAmount(findOne.getAmount());
		dto.setEmployeId(noteDeFrais.getEmploye().getId());
		dto.setEmployeName(noteDeFrais.getEmploye().getFullName());
		dto.setId(findOne.getId());
		dto.setValidationStatus(findOne.getValidationStatus());
		List<Long> attachementIds=new ArrayList<>();
		for(Attachement attachement: findOne.getAttachements()){
			attachementIds.add(attachement.getId());
		}
		dto.setAttachementsIds(attachementIds);
		return dto;
	}

	@Override
	public Page<NoteDeFrais> findAll(Specification<NoteDeFrais> spec,
			Pageable pageable) {
		return noteDeFraisRepository.findAll(spec, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.SpecificationFactory#buildSpecification(fr.wati.yacramanager.utils.Filter)
	 */
	@Override
	public Specification<NoteDeFrais> buildSpecification(Filter filter) {
		if(filter!=null){
			FilterType filterType = filter.getType();
			switch (filterType) {
			case ARRAY:
				FilterArray filterArray=(FilterArray) filter;
				if("employe".equals(filter.getField())){
					List<Employe> employes=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						employes.add(employeService.findOne(Long.valueOf(filterArrayValue.getName())));
					}
					return CommonSpecifications.equalsAny(employes, NoteDeFrais_.employe);
				}
				break;
			case TEXT:
				FilterText filterText=(FilterText) filter;
				if("description".equals(filterText.getField())){
					return CommonSpecifications.likeIgnoreCase(filterText.getValue(), NoteDeFrais_.description);
				}
				break;
			case BOOLEAN:
				
			case DATE:
			case DATE_RANGE:
				FilterDate filterDate=(FilterDate) filter;
				if("date".equals(filter.getField())){
					if(filterDate.isRangedDate()){
						return CommonSpecifications.betweenDate(filterDate.getValue().getStart(), filterDate.getValue().getEnd(), NoteDeFrais.class,"date");
					}else {
						return CommonSpecifications.equals(filterDate.getValue().getDate(), NoteDeFrais_.date);
					}
				}
				break;
			case COMPARATOR_BETWEEN:
			case COMPARATOR_EQUALS:
			case COMPARATOR_GREATERTHAN:
			case COMPARATOR_LESSTHAN:
				FilterComparator filterComparator= (FilterComparator) filter;
				if("amount".equals(filter.getField())){
					switch (filterComparator.getComparator()) {
					case EQUALS:
						return CommonSpecifications.equals(filterComparator.getValue().getValue(), NoteDeFrais_.amount);
					case GREATERTHAN:
						return CommonSpecifications.greaterThan(filterComparator.getValue().getValue(), NoteDeFrais_.amount);
					case LESSTHAN:
						return CommonSpecifications.lessThan(filterComparator.getValue().getValue(), NoteDeFrais_.amount);
					case BETWEEN:
						return CommonSpecifications.between(filterComparator.getValue().getStartValue(), filterComparator.getValue().getEndValue(), NoteDeFrais_.amount);
					default:
						break;
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
	public void validate(Employe validator, NoteDeFrais noteDeFrais) throws ServiceException {
		NoteDeFrais findOne = noteDeFraisRepository.findOne(noteDeFrais.getId());
		if(employeService.isManager(validator.getId(), findOne.getEmploye().getId())){
			findOne.setValidationStatus(ValidationStatus.APPROVED);
			NoteDeFrais save = save(findOne);
			applicationEventPublisher.publishEvent(ActivityEvent
					.createWithSource(this).user(validator)
					.operation(ActivityOperation.VALIDATE)
					.onEntity(NoteDeFrais.class, save.getId()));
		}else {
			throw new ServiceException(validator.getFullName()+ " is not the manager of "+findOne.getEmploye().getFullName());
		}
	}

	@Override
	public void reject(Employe validator, NoteDeFrais noteDeFrais) throws ServiceException{
		NoteDeFrais findOne = noteDeFraisRepository.findOne(noteDeFrais.getId());
		if(employeService.isManager(validator.getId(), findOne.getEmploye().getId())){
			findOne.setValidationStatus(ValidationStatus.REJECTED);
			NoteDeFrais save = save(findOne);
			applicationEventPublisher.publishEvent(ActivityEvent
					.createWithSource(this).user(validator)
					.operation(ActivityOperation.REJECT)
					.onEntity(NoteDeFrais.class, save.getId()).dto(map(save)));
		}else {
			throw new ServiceException(validator.getFullName()+ " is not the manager of "+findOne.getEmploye().getFullName());
		}
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.StatusValidator#getEntitiesToApproved(java.lang.Long)
	 */
	@Override
	public List<NoteDeFrais> getEntitiesToApproved(Long employeId) {
		List<Employe> managedEmployes=employeService.getManagedEmployees(employeId);
		List<NoteDeFrais> noteDeFraisToApproved=new ArrayList<>();
		for (Employe employe : managedEmployes) {
			Specifications<NoteDeFrais> specifications = Specifications.where(CommonSpecifications.equals(employe, NoteDeFrais_.employe))
			.and(CommonSpecifications.equalsAny(Lists.newArrayList(ValidationStatus.PENDING), NoteDeFrais_.validationStatus));
			noteDeFraisToApproved.addAll(noteDeFraisRepository.findAll(specifications));
		}
		return noteDeFraisToApproved;
	}
	
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

}
