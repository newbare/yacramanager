package fr.wati.yacramanager.services;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Employe;

public interface ActivityReportService {

	void submitNewActivityReport(Employe employe,DateTime startDate,DateTime endDate) throws ServiceException;
}
