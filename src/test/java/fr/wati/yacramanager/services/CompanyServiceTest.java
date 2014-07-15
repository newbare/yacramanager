package fr.wati.yacramanager.services;

import static org.springframework.util.Assert.*;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.config.TestServicesConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestServicesConfig.class})
public class CompanyServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private CompanyService companyService;
	
	@Test
	public void testCreateCompany(){
		Company company=new Company();
		company.setName("Mock company");
		companyService.createCompany(company);
		isTrue(companyService.exists(company.getId()));
		notNull(company.getClients().get(0).getProjects().get(0).getTasks().get(0));
	}
	
	
	@Test
	public void testDeleteCompany(){
		Company company=new Company();
		company.setName("Mock company");
		Company save = companyService.save(company);
		isTrue(companyService.exists(save.getId()));
		companyService.delete(save.getId());
		isNull(companyService.findOne(save.getId()));
	}
	
	@Test
	public void testUpdateCompany(){
		Company company=new Company();
		company.setName("Mock company");
		Company saveCompany = companyService.save(company);
		isTrue(companyService.exists(saveCompany.getId()));
		saveCompany.setName("Updated");
		companyService.save(saveCompany);
		Assert.assertEquals("Updated", companyService.findOne(saveCompany.getId()).getName()); 
	}
	
	
}