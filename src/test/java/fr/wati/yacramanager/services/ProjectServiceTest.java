package fr.wati.yacramanager.services;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.util.Assert.*;
import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.config.TestServicesConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestServicesConfig.class})
public class ProjectServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private ClientService clientService;
	@Autowired
	private CompanyService companyService;
	
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
	
}
