package fr.wati.yacramanager.services;

import java.util.List;

import org.joda.time.LocalDate;

import fr.wati.yacramanager.beans.ActivityReport;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.ValidationStatus;

public interface ActivityReportService extends CrudService<ActivityReport, Long> {

	void submitNewActivityReport(Employe employe,LocalDate startDate,LocalDate endDate) throws ServiceException;
	
	void cancelSubmittedActivityReport(Employe employe,LocalDate startDate,LocalDate endDate) throws ServiceException;
	
	void approveSubmittedActivityReport(Employe employeManager,ActivityReport activityReport) throws ServiceException;
	
	void rejectSubmittedActivityReport(Employe employeManager,ActivityReport activityReport) throws ServiceException;
	/**
	 * @param employe
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ServiceException
	 */
	List<ActivityReport> findByEmployeAndStartDateBetweenAndEndDateBetween(
			Employe employe, LocalDate startDate, LocalDate endDate)
			throws ServiceException;
	
	
	List<ActivityReport> findApprovedBetweenDate(Long employeId,LocalDate startDate,LocalDate endDate,List<ValidationStatus> validationStatus);
	
	ActivityReport findByEmployeAndStartDateAndEndDate(Employe employe,LocalDate startDate,LocalDate endDate);
	List<ActivityReport> findByEmployeInAndValidationStatusIn(List<Long> employeId,List<ValidationStatus> validationStatus);
}
