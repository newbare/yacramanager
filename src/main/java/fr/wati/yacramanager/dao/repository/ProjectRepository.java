package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>,JpaSpecificationExecutor<Project> {

	Project findByClientAndId(Client client,Long id);
	
	@Query("select p from Project p,Company comp,Client client  where comp.id = ?1 and client.company = comp and p.client =client")
	Page<Project> find(Long companyId, Specification<Project> specification,Pageable pageable);
	
	List<Project> findByAssignedEmployeesIn(Employe employe);
	
}
