package fr.wati.yacramanager.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.WorkLog;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long>,JpaSpecificationExecutor<WorkLog> {

	Page<WorkLog> findByStartDateBetween(Date dateDebut, Date dateFin,
			Pageable pageable);

	Page<WorkLog> findByEmployeAndStartDateBetween(Employe employe,
			Date dateDebut, Date dateFin, Pageable pageable);

	List<WorkLog> findByEmployeAndStartDateBetween(Employe employe,
			Date dateDebut, Date dateFin);

	Page<WorkLog> findByEmploye(Employe employe, Pageable pageable);

}
