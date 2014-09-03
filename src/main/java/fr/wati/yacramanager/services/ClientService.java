package fr.wati.yacramanager.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.web.dto.ClientDTO;

public interface ClientService extends CrudService<Client, Long>,SpecificationFactory<Client> {

	Client createClient(Long companyId,Client client);
	void deleteClient(Long companyId,Long clientId);
	Client findByCompanyAndId(Company company, Long id);
	ClientDTO toClientDTO(Client client);
	List<ClientDTO> toClientDTOs(Iterable<Client> clients);
	Page<Client> findByCompany(Company company, Pageable pageable);
}
