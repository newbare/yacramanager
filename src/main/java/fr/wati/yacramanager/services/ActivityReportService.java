package fr.wati.yacramanager.services;

import java.util.List;

import org.joda.time.LocalDate;

import fr.wati.yacramanager.beans.ActivityReport;
import fr.wati.yacramanager.beans.Employe;

public interface ActivityReportService {

	void submitNewActivityReport(Employe employe,LocalDate startDate,LocalDate endDate) throws ServiceException;
	
	List<ActivityReport> findByEmployeAndStartDateBetweenOrEndDateDateBetween(Employe employe,LocalDate startDate,LocalDate endDate) throws ServiceException;
}
