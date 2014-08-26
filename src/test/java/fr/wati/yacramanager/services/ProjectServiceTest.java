package fr.wati.yacramanager.services;

import static org.springframework.util.Assert.isNull;
import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Company_;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.config.TestServicesConfig;
import fr.wati.yacramanager.dao.repository.ProjectRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.dao.specifications.ProjectSpecification;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestServicesConfig.class})
public class ProjectServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ClientService clientService;
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private EmployeService employeService;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Test
	public void testCreateClient(){
		//create company
		Company company=new Company();
		company.setName("Company1");
		Company saveCompany = companyService.save(company);
		Client client=new Client();
		client.setName("Client1");
		Client createClient = clientService.createClient(saveCompany.getId(), client);
		Assert.assertEquals("Client1", createClient.getName());
		notNull(createClient.getId());
		notNull(createClient.getCompany());
		notEmpty(saveCompany.getClients());
	}
	
	@Test
	public void testDeleteClient(){
		Company company=new Company();
		company.setName("Company1");
		Company saveCompany = companyService.save(company);
		Client client=new Client();
		client.setName("Client2");
		Client createClient = clientService.createClient(saveCompany.getId(), client);
		clientService.deleteClient(saveCompany.getId(), createClient.getId());
		isNull(clientService.findOne(createClient.getId()));
	}
	
	@Test
	public void projectFromCompany(){
		//create company
		Company company=new Company();
		company.setName("Company1");
		Company saveCompany = companyService.save(company);
		Client client=new Client();
		client.setName("Client1");
		Client createClient = clientService.createClient(saveCompany.getId(), client);
		Assert.assertEquals("Client1", createClient.getName());
		notNull(createClient.getId());
		notNull(createClient.getCompany());
		notEmpty(saveCompany.getClients());
		Pageable pageable=new PageRequest(0,10);
		Company company2=new Company();
		company2.setName("Company2");
		Company saveCompany2 = companyService.save(company2);
		Client client2=new Client();
		client2.setName("Client2");
		clientService.createClient(saveCompany2.getId(), client2);
		Page<Project> page = projectRepository.findAll(Specifications.where(ProjectSpecification.findForCompany(saveCompany.getId())), pageable);
		Assert.assertTrue(page.getTotalElements()==1);
	}
	
	@Test
	public void globalSearch(){
		//create company
		Company company=new Company();
		company.setName("Company1");
		Company saveCompany = companyService.save(company);
		Page<Company> page = companyService.findAll(CommonSpecifications.globalSearch("Com", Company.class,Company_.class), new PageRequest(0,10));
		Assert.assertTrue(page.getTotalElements()==1);
	}
	
	@Test
	public void findProjectByEmploye(){
		//create company
		Company company=new Company();
		company.setName("Company1");
		Company saveCompany = companyService.createCompany(company);
		
		Employe managedEmploye1 = new Employe();
		managedEmploye1.setLastName("managedEmploye1");
		managedEmploye1.setFirstName("managedEmploye1");
		employeService.save(managedEmploye1);
		saveCompany.getClients().get(0).getProjects().get(0).getAssignedEmployees().add(managedEmploye1);
		List<Project> findByAssignedEmployeesIn = projectRepository.findByAssignedEmployeesIn(managedEmploye1);
		Assert.assertTrue(1==findByAssignedEmployeesIn.size());
	}
}
