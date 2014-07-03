package fr.wati.yacramanager.services;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Absence;

public interface AbsenceService extends CrudService<Absence, Long>{
	
	Page<Absence> findByStartDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	
	void validateAbsence(Absence absence);
}
