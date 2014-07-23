package fr.wati.yacramanager.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>,JpaSpecificationExecutor<Project> {

	Project findByClientAndId(Client client,Long id);
}
