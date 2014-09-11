/**
 * 
 */
package fr.wati.yacramanager.services;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.wati.yacramanager.beans.AbsencePortfolio;
import fr.wati.yacramanager.beans.AbsencePortfolio.AbsencePortfolioPK;
import fr.wati.yacramanager.beans.Contact;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.config.TestServicesConfig;
import fr.wati.yacramanager.dao.repository.AbsencePortfolioRepository;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

/**
 * @author Rachid Ouattara
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServicesConfig.class })
public class AbsencePortfolioTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private AbsencePortfolioRepository absencePortfolioRepository;
	
	@Test
	public void testCreateAndRetrieve() throws ServiceException {
		Employe manager = new Employe();
		manager.setLastName("Manager");
		Contact contact=new Contact();
		contact.setEmail("blabla@blabla.com");
		manager.setContact(contact);;
		manager.setFirstName("Manager");
		employeService.save(manager);
		
		AbsencePortfolio absencePortfolio=new AbsencePortfolio();
		absencePortfolio.setRemaining(14L);
		absencePortfolio.setConsumed(3L);
		AbsencePortfolioPK absencePortfolioPK=new AbsencePortfolioPK(manager.getId(), TypeAbsence.CP);
		absencePortfolio.setAbsencePortfolioPK(absencePortfolioPK);
		absencePortfolioRepository.save(absencePortfolio);
		
		AbsencePortfolio findByUserAndType = absencePortfolioRepository.findByUserAndType(manager.getId(), TypeAbsence.CP);
		Assert.assertTrue(findByUserAndType!=null);
		absencePortfolioRepository.findByUser(manager.getId());
	}
}
