package fr.wati.yacramanager.services;

import java.util.List;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.wati.yacramanager.beans.ActivityReport;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.config.Constants;
import fr.wati.yacramanager.config.TestServicesConfig;
import fr.wati.yacramanager.dao.repository.ActivityReportRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServicesConfig.class })
@ActiveProfiles(value={Constants.SPRING_PROFILE_TEST})
public class ActivityReportTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private ActivityReportService activityReportService;
	
	@Autowired
	private ActivityReportRepository activityReportRepository;
	
	@Test
	public void testCreateAndRetrieve() throws ServiceException {
		Employe manager = new Employe();
		manager.setLastName("Manager");
		manager.setFirstName("Manager");
		Employe employe = employeService.save(manager);
		
		activityReportService.submitNewActivityReport(employe, new LocalDate(2014,9,01), new LocalDate(2014,9,30));
		
		List<ActivityReport> findByEmployeAndStartDateBetweenOrEndDateDateBetween = activityReportService.findByEmployeAndStartDateBetweenAndEndDateBetween(employe, new LocalDate(2014,9,1), new LocalDate(2014,9,30));
		Assert.assertTrue(findByEmployeAndStartDateBetweenOrEndDateDateBetween!=null && findByEmployeAndStartDateBetweenOrEndDateDateBetween.size()>0);
		findByEmployeAndStartDateBetweenOrEndDateDateBetween = activityReportService.findByEmployeAndStartDateBetweenAndEndDateBetween(employe, new LocalDate(2014,9,1), new LocalDate(2014,9,29));
		Assert.assertTrue(findByEmployeAndStartDateBetweenOrEndDateDateBetween!=null && findByEmployeAndStartDateBetweenOrEndDateDateBetween.size()==0);
		
		ActivityReport findByEmployeAndStartDateAndEndDate = activityReportRepository.findByEmployeAndStartDateAndEndDate(employe, new LocalDate(2014,9,01), new LocalDate(2014,9,30));
		Assert.assertNotNull(findByEmployeAndStartDateAndEndDate);
	}

}
