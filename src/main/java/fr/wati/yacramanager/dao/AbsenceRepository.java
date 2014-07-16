package fr.wati.yacramanager.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Employe;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

	Page<Absence> findByDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	Page<Absence> findByEmployeAndStartDateBetween(Employe employe,Date dateDebut,Date dateFin,Pageable pageable);
	
	List<Absence> findByEmployeAndStartDateBetween(Employe employe,Date dateDebut,Date dateFin);
	
	Page<Absence> findByEmploye(Employe employe,Pageable pageable);
}
