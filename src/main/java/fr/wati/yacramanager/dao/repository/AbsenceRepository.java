package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Employe;

public interface AbsenceRepository extends JpaRepository<Absence, Long>, JpaSpecificationExecutor<Absence> {

	Page<Absence> findByDateBetween(DateTime DateTimeDebut,DateTime DateTimeFin,Pageable pageable);
	
	Page<Absence> findByEmployeAndStartDateBetween(Employe employe,DateTime DateTimeDebut,DateTime DateTimeFin,Pageable pageable);
	
	List<Absence> findByEmployeAndStartDateBetween(Employe employe,DateTime DateTimeDebut,DateTime DateTimeFin);
	
	Page<Absence> findByEmploye(Employe employe,Pageable pageable);
}
