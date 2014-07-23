package fr.wati.yacramanager.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.dao.repository.ClientRepository;
import fr.wati.yacramanager.dao.repository.CompanyRepository;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.web.dto.ClientDTO;

@Transactional
@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private ProjectService projectService;
	
	@Override
	public <S extends Client> S save(S entity) {
		return clientRepository.save(entity);
	}

	@Override
	public <S extends Client> Iterable<S> save(Iterable<S> entities) {
		return clientRepository.save(entities);
	}

	@Override
	public Client findOne(Long id) {
		return clientRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return clientRepository.exists(id);
	}

	@Override
	public Iterable<Client> findAll() {
		return clientRepository.findAll();
	}

	@Override
	public Iterable<Client> findAll(Iterable<Long> ids) {
		return clientRepository.findAll(ids);
	}

	@Override
	public long count() {
		return clientRepository.count();
	}

	@Override
	public void delete(Long id) {
		clientRepository.delete(id);
	}

	@Override
	public void delete(Client entity) {
		clientRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Client> entities) {
		clientRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		clientRepository.deleteAll();
	}

	public Client createClient(Long companyId,Client client){
		Company company=companyRepository.findOne(companyId);
		client.setCompany(company);
		Client saveClient = clientRepository.save(client);
		company.getClients().add(saveClient);
		/*
		 * Each client should have a default project
		 */
		Project project=new Project();
		project.setName("Default Project ("+saveClient.getName()+")");
		projectService.createProject(saveClient.getId(), project);
		return saveClient;
	}
	
	public void deleteClient(Long companyId,Long clientId){
		Company company=companyRepository.findOne(companyId);
		Client client = clientRepository.findByCompanyAndId(company, clientId);
		clientRepository.delete(client);
	}

	@Override
	public Client findByCompanyAndId(Company company, Long id) {
		return clientRepository.findByCompanyAndId(company, id);
	}

	@Override
	public ClientDTO toClientDTO(Client client) {
		ClientDTO clientDTO=new ClientDTO();
		clientDTO.setName(client.getName());
		clientDTO.setId(client.getId());
		return clientDTO;
	}

	@Override
	public Page<Client> findByCompany(Company company, Pageable pageable) {
		return clientRepository.findByCompany(company, pageable);
	}

	@Override
	public Page<Client> findAll(Specification<Client> spec, Pageable pageable) {
		return clientRepository.findAll(spec, pageable);
	}
}
