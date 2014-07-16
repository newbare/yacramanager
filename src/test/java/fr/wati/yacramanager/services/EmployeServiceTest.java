/**
 * 
 */
package fr.wati.yacramanager.services;

import java.util.Date;

import junit.framework.Assert;

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
@ContextConfiguration(classes={TestServicesConfig.class})
public class EmployeServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private EmployeService employeService;
	@Autowired
	private CompanyService companyService;
	
	@Test
	public void testDozerMappingConfig() throws Exception{
		Company company=new Company();
		company.setName("company-test");
		Company createCompany = companyService.createCompany(company);
		Employe employe=new Employe();
		employe.setNom("name");
		employe.setPrenom("prenom");
		employe.setUsername("username");
		employe.setPassword("password");
		employe.setDateNaissance(new Date());
		employe.setCompany(createCompany);
		Employe saveEmploye = employeService.save(employe);
		UserInfoDTO userInfoDTO = employeService.toUserInfoDTO(saveEmploye.getId());
		Assert.assertNotNull(userInfoDTO.getCompany());
	}
}
