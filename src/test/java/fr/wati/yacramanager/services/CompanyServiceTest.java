package fr.wati.yacramanager.services;

import static org.springframework.util.Assert.isNull;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.util.Calendar;
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
import fr.wati.yacramanager.dao.repository.CompanyRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.dao.specifications.CompanySpecifications;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestServicesConfig.class})
public class CompanyServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private CompanyRepository companyRepository;
	
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
	
	@Test
	public void testSearchByName(){
		Company company=new Company();
		company.setName("Mock company");
		companyService.createCompany(company);
		List<Company> findAll = companyRepository.findAll(CompanySpecifications.namelike("ck"));
		Assert.assertTrue(findAll.size()>0);
	}
	
	@Test
	public void testRegisteredBetween(){
		Company company=new Company();
		company.setName("Mock company");
		company.setRegisteredDate(new DateTime(2014, 7, 14, 0, 0));
		companyService.createCompany(company);
		List<Company> findAll = companyRepository.findAll(CompanySpecifications.registeredDateBetween(new DateTime(2014, 7, 12, 0, 0), new DateTime(2014, 7, 16, 0, 0)));
		Assert.assertTrue(findAll.size()>0);
		findAll=companyRepository.findAll(CommonSpecifications.betweenDate(new DateTime(2014, 7, 12, 0, 0), new DateTime(2014, 7, 16, 0, 0), Company.class, "registeredDate"));
		Assert.assertTrue(findAll.size()>0);
		findAll=companyRepository.findAll(CommonSpecifications.betweenDate(new DateTime(2014, 7, 15, 0, 0), new DateTime(2014, 7, 16, 0, 0), Company.class, "registeredDate"));
		Assert.assertTrue(findAll.size()==0);
	}
	
	@Test
	public void testCompanyFromEmploye(){
		Employe employe=new Employe();
		employe.setUserName("toto@toto.com");
		employe.setPassword("fdsf");
		Company company=new Company();
		company.setName("Mock company");
		Calendar calendar=Calendar.getInstance();
		calendar.set(2014, 07, 14);
		company.setRegisteredDate(new DateTime(calendar.getTime()));
		companyService.createCompany(company);
		employe.setCompany(company);
		employeService.save(employe);
		Calendar startCalendar=Calendar.getInstance();
		startCalendar.set(2014, 07, 12);
		Calendar endCalendar=Calendar.getInstance();
		endCalendar.set(2014, 07, 16);
		Company findOne = companyRepository.findOne(CompanySpecifications.employeMemberOfCompany(employe));
		Assert.assertNotNull(findOne);
	}
	
}
