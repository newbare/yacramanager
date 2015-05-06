package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.WorkLog;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long>,JpaSpecificationExecutor<WorkLog> {

	Page<WorkLog> findByStartDateBetween(LocalDateTime dateDebut, LocalDateTime dateFin,
			Pageable pageable);

	Page<WorkLog> findByEmployeAndStartDateBetween(Employe employe,
			LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable);
	
	Page<WorkLog> findByEmployeAndStartDateBetweenAndExtraTimeTrue(Employe employe,
			LocalDateTime dateDebut, LocalDateTime dateFin, Pageable pageable);

	List<WorkLog> findByEmployeAndStartDateBetween(Employe employe,
			LocalDateTime dateDebut, LocalDateTime dateFin);
	
	List<WorkLog> findByEmployeAndStartDateBetweenAndExtraTimeFalse(Employe employe,
			LocalDateTime dateDebut, LocalDateTime dateFin);
	
	List<WorkLog> findByEmployeAndStartDateBetweenAndExtraTimeTrue(Employe employe,
			LocalDateTime dateDebut, LocalDateTime dateFin);

	Page<WorkLog> findByEmploye(Employe employe, Pageable pageable);

}
