package fr.wati.yacramanager.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Personne;

public interface AbsenceService extends CrudService<Absence, Long>{
	
	Page<Absence> findByStartDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	Page<Absence> findByPersonneAndStartDateBetween(Personne personne,Date dateDebut,Date dateFin,Pageable pageable);
	
	List<Absence> findByPersonneAndStartDateBetween(Personne personne,Date dateDebut,Date dateFin);
	
	Page<Absence> findByPersonne(Personne personne,Pageable pageable);
	
	void validateAbsence(Absence absence);
}
