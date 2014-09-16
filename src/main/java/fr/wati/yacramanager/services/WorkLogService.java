package fr.wati.yacramanager.services;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.WorkLog;

public interface WorkLogService extends CrudService<WorkLog, Long>,SpecificationFactory<WorkLog>,StatusValidator<WorkLog, Employe>{
	
	Page<WorkLog> findByStartDateBetween(DateTime dateDebut,DateTime dateFin,Pageable pageable);
	
	Page<WorkLog> findByEmployeAndStartDateBetween(Employe employe,DateTime dateDebut,DateTime dateFin,Pageable pageable);
	
	Page<WorkLog> findExtraTime(Employe employe,DateTime dateDebut,DateTime dateFin,Pageable pageable);
	
	List<WorkLog> findByEmployeAndStartDateBetween(Employe employe,DateTime dateDebut,DateTime dateFin);

	List<WorkLog> findByEmployeAndStartDateBetweenAndExtratimeFalse(Employe employe,DateTime dateDebut,DateTime dateFin);
	
	List<WorkLog> findExtraTime(Employe employe,DateTime dateDebut,DateTime dateFin);
	
	Page<WorkLog> findByEmploye(Employe employe,Pageable pageable);
	
}
