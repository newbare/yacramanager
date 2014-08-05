package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Absence_;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.dao.repository.AbsenceRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterBoolean;
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

	@Override
	public Page<Absence> findByStartDateBetween(DateTime dateDebut, DateTime dateFin,
			Pageable pageable) {
		return absenceRepository.findByDateBetween(dateDebut, dateFin, pageable);
	}


	@Override
	public void validateAbsence(Absence absence) {
		// TODO Auto-generated method stub

	}


	@Override
	public <S extends Absence> S save(S entity) {
		return absenceRepository.save(entity);
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
			case BOOLEAN:
				FilterBoolean filterBoolean=(FilterBoolean) filter;
				if("validated".equals(filter.getField())){
					if(filterBoolean.isValue()){
						return CommonSpecifications.isTrue(Absence_.validated);
					}else {
						return CommonSpecifications.isFalse(Absence_.validated);
					}
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

}
