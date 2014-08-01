package fr.wati.yacramanager.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.WorkLog;

public interface WorkLogService extends CrudService<WorkLog, Long>,SpecificationFactory<WorkLog>{
	
	Page<WorkLog> findByStartDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	Page<WorkLog> findByEmployeAndStartDateBetween(Employe employe,Date dateDebut,Date dateFin,Pageable pageable);
	
	List<WorkLog> findByEmployeAndStartDateBetween(Employe employe,Date dateDebut,Date dateFin);
	
	Page<WorkLog> findByEmploye(Employe employe,Pageable pageable);
	
}
