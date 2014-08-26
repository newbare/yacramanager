/**
 * 
 */
package fr.wati.yacramanager.services;

import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.config.TestServicesConfig;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

/**
 * @author Rachid Ouattara
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestServicesConfig.class })
public class EmployeServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private EmployeService employeService;
	@Autowired
	private CompanyService companyService;

	@Test
	public void testDozerMappingConfig() throws Exception {
		Company company = new Company();
		company.setName("company-test");
		Company createCompany = companyService.createCompany(company);
		Employe employe = new Employe();
		employe.setLastName("name");
		employe.setFirstName("prenom");
		employe.setUsername("username");
		employe.setPassword("password");
		employe.setBirthDay(new DateTime());
		employe.setCompany(createCompany);
		Employe saveEmploye = employeService.save(employe);
		Employe manager = new Employe();
		manager.setLastName("Manager");
		manager.setFirstName("Manager");
		employeService.save(manager);
		employeService.addManagedEmploye(manager.getId(), saveEmploye.getId());
		UserInfoDTO userInfoDTO = employeService.toUserInfoDTO(saveEmploye
				.getId());
		Assert.assertNotNull(userInfoDTO.getCompany());
	}

	@Test
	public void testRetrieveManagedEmployees() {
		Employe manager = new Employe();
		manager.setLastName("Manager");
		manager.setFirstName("Manager");
		employeService.save(manager);
		Employe managedEmploye1 = new Employe();
		managedEmploye1.setLastName("managedEmploye1");
		managedEmploye1.setFirstName("managedEmploye1");
		employeService.save(managedEmploye1);
		employeService.addManagedEmploye(manager.getId(), managedEmploye1.getId());
		Employe managedEmploye2 = new Employe();
		managedEmploye2.setLastName("managedEmploye2");
		managedEmploye2.setFirstName("managedEmploye2");
		employeService.save(managedEmploye2);
		employeService.addManagedEmploye(manager.getId(), managedEmploye2.getId());
		List<Employe> managedEmployes=employeService.getManagedEmployees(manager.getId());
		Assert.assertTrue(managedEmployes.size()==2);
		
	}
}
