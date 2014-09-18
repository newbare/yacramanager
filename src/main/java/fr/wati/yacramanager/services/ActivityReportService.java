package fr.wati.yacramanager.services;

import org.joda.time.LocalDate;

import fr.wati.yacramanager.beans.Employe;

public interface ActivityReportService {

	void submitNewActivityReport(Employe employe,LocalDate startDate,LocalDate endDate) throws ServiceException;
}
