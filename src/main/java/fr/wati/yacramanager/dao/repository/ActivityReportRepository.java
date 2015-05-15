package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.wati.yacramanager.beans.ActivityReport;
import fr.wati.yacramanager.beans.ValidationStatus;

public interface ActivityReportRepository extends
		JpaRepository<ActivityReport, Long>,
		JpaSpecificationExecutor<ActivityReport> {

	List<ActivityReport> findByEmployeIdAndStartDateBetweenAndEndDateBetween(
			Long employeId, LocalDate startDate, LocalDate endDate,
			LocalDate startDate2, LocalDate endDate2);

	List<ActivityReport> findByEmployeIdAndStartDateBetweenAndEndDateBetweenAndValidationStatusIn(
			Long employeId, LocalDate startDate, LocalDate endDate,
			LocalDate startDate2, LocalDate endDate2,
			List<ValidationStatus> validationStatus);

	@Query("SELECT a FROM  ActivityReport a where"
			+ " a.employeId=:employeId and "
			+ "(:startDate between a.startDate and a.endDate) and "
			+ "(:endDate between a.startDate and a.endDate) and "
			+ "a.validationStatus in (:validationStatus)")
	List<ActivityReport> findApprovedBetweenDate(@Param("employeId") Long employeId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
			@Param("validationStatus") List<ValidationStatus> validationStatus);

	ActivityReport findByEmployeIdAndStartDateAndEndDate(Long employeId,
			LocalDate startDate, LocalDate endDate);

	List<ActivityReport> findByEmployeIdInAndValidationStatusIn(
			List<Long> employeIds, List<ValidationStatus> validationStatus);
}
