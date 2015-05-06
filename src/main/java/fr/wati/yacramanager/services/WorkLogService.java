package fr.wati.yacramanager.services;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.WorkLog;

public interface WorkLogService extends CrudService<WorkLog, Long>,SpecificationFactory<WorkLog>,StatusValidator<WorkLog, Employe>{
	
	Page<WorkLog> findByStartDateBetween(LocalDateTime dateDebut,LocalDateTime dateFin,Pageable pageable);
	
	Page<WorkLog> findByEmployeAndStartDateBetween(Employe employe,LocalDateTime dateDebut,LocalDateTime dateFin,Pageable pageable);
	
	Page<WorkLog> findExtraTime(Employe employe,LocalDateTime dateDebut,LocalDateTime dateFin,Pageable pageable);
	
	List<WorkLog> findByEmployeAndStartDateBetween(Employe employe,LocalDateTime dateDebut,LocalDateTime dateFin);

	List<WorkLog> findByEmployeAndStartDateBetweenAndExtratimeFalse(Employe employe,LocalDateTime dateDebut,LocalDateTime dateFin);
	
	List<WorkLog> findExtraTime(Employe employe,LocalDateTime dateDebut,LocalDateTime dateFin);
	
	Page<WorkLog> findByEmploye(Employe employe,Pageable pageable);
	
}
