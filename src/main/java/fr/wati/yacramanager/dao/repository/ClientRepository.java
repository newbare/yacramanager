package fr.wati.yacramanager.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;

public interface ClientRepository extends JpaRepository<Client, Long>,JpaSpecificationExecutor<Client> {

	Client findByCompanyAndId(Company company, Long clientId);
	Page<Client> findByCompany(Company company, Pageable pageable);

}
