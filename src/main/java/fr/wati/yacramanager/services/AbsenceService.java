package fr.wati.yacramanager.services;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Employe;

public interface AbsenceService extends CrudService<Absence, Long>,SpecificationFactory<Absence>,StatusValidator<Absence, Employe>{
	
	Page<Absence> findByStartDateBetween(LocalDate startDate,LocalDate endDate,Pageable pageable);
	
	Page<Absence> findByEmployeAndStartDateBetween(Employe employe,LocalDate startDate,LocalDate endDate,Pageable pageable);
	
	List<Absence> findByEmployeAndStartDateBetween(Employe employe,LocalDate startDate,LocalDate endDate);
	
	Page<Absence> findByEmploye(Employe employe,Pageable pageable);
	
	Absence postAbsence(Long employeId, Absence absence) throws ServiceException;
	
	void periodicalyIncrementAbsence();
}
