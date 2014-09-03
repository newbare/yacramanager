package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Client_;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.dao.repository.ClientRepository;
import fr.wati.yacramanager.dao.repository.CompanyRepository;
import fr.wati.yacramanager.dao.specifications.CommonSpecifications;
import fr.wati.yacramanager.listeners.ActivityEvent;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.utils.Filter.FilterArray;
import fr.wati.yacramanager.utils.Filter.FilterArrayValue;
import fr.wati.yacramanager.utils.Filter.FilterText;
import fr.wati.yacramanager.utils.Filter.FilterType;
import fr.wati.yacramanager.web.dto.ClientDTO;
import fr.wati.yacramanager.web.dto.ContactDTO;

@Transactional
@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private DtoMapper dtoMapper;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProjectService projectService;

	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public <S extends Client> S save(S entity) {
		S save = clientRepository.save(entity);
		applicationEventPublisher.publishEvent(ActivityEvent
				.createWithSource(this).user()
				.operation(ActivityOperation.CREATE)
				.onEntity(Client.class, save.getId()));
		return save;
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
		project.setName(client.getName()+" - default");
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
		clientDTO.setContacts(dtoMapper.mapContacts(client));
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

	@Override
	public Specification<Client> buildSpecification(Filter filter) {
		if(filter!=null){
			FilterType filterType = filter.getType();
			switch (filterType) {
			case ARRAY:
				FilterArray filterArray=(FilterArray) filter;
				if("company".equals(filter.getField())){
					List<Company> companies=new ArrayList<>();
					for(FilterArrayValue filterArrayValue: filterArray.getValue()){
						companies.add(companyService.findOne(Long.valueOf(filterArrayValue.getName())));
					}
					return CommonSpecifications.equalsAny(companies, Client_.company);
				}
				break;
			case TEXT:
				FilterText filterText=(FilterText) filter;
				if("name".equals(filterText.getField())){
					return CommonSpecifications.likeIgnoreCase(filterText.getValue(), Client_.name);
				}
				break;
			default:
				break;
			}
		}
		return null;
	}
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	@Override
	public List<ClientDTO> toClientDTOs(Iterable<Client> clients) {
		List<ClientDTO> clientDTOs=new ArrayList<>();
		for (Client client : clients) {
			clientDTOs.add(toClientDTO(client));
		}
		return clientDTOs;
	}
}
