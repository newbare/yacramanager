package fr.wati.yacramanager.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Employe;

public interface AbsenceService extends CrudService<Absence, Long>{
	
	Page<Absence> findByStartDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	Page<Absence> findByEmployeAndStartDateBetween(Employe employe,Date dateDebut,Date dateFin,Pageable pageable);
	
	List<Absence> findByEmployeAndStartDateBetween(Employe employe,Date dateDebut,Date dateFin);
	
	Page<Absence> findByEmploye(Employe employe,Pageable pageable);
	
	void validateAbsence(Absence absence);
}
