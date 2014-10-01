package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.ActivityReport;
import fr.wati.yacramanager.beans.Employe;

public interface ActivityReportRepository extends JpaRepository<ActivityReport, Long>, JpaSpecificationExecutor<ActivityReport>{

	List<ActivityReport> findByEmployeAndStartDateBetweenOrEndDateBetween(Employe employe,LocalDate startDate,LocalDate endDate,LocalDate startDate2,LocalDate endDate2);
}
