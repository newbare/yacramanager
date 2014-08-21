package fr.wati.yacramanager.services;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Employe;

public interface AbsenceService extends CrudService<Absence, Long>,SpecificationFactory<Absence>,StatusValidator<Absence, Employe>{
	
	Page<Absence> findByStartDateBetween(DateTime DateTimeDebut,DateTime DateTimeFin,Pageable pageable);
	
	Page<Absence> findByEmployeAndStartDateBetween(Employe employe,DateTime DateTimeDebut,DateTime DateTimeFin,Pageable pageable);
	
	List<Absence> findByEmployeAndStartDateBetween(Employe employe,DateTime DateTimeDebut,DateTime DateTimeFin);
	
	Page<Absence> findByEmploye(Employe employe,Pageable pageable);
}
