package fr.wati.yacramanager.services.impl;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.ActivityReport;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.dao.repository.ActivityReportRepository;
import fr.wati.yacramanager.services.ActivityReportService;
import fr.wati.yacramanager.services.ServiceException;

@Service
@Transactional
public class ActivityReportServiceImpl implements ActivityReportService {
	@Autowired
	private ActivityReportRepository activityReportRepository;

	public ActivityReportServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void submitNewActivityReport(Employe employe, DateTime startDate,
			DateTime endDate) throws ServiceException {
		ActivityReport activityReport=new ActivityReport();
		activityReport.setEmploye(employe);
		activityReport.setStartDate(startDate);
		activityReport.setEndDate(endDate);
		activityReport.setValidationStatus(ValidationStatus.WAIT_FOR_APPROVEMENT);
		activityReportRepository.save(activityReport);
	}

}
