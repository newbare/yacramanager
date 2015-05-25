/**
 * 
 */
package fr.wati.yacramanager.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.EmployesProjects;
import fr.wati.yacramanager.beans.EmployesProjectsId;

/**
 * @author Rachid Ouattara
 *
 */
public interface EmployesProjectsRepository extends JpaRepository<EmployesProjects, EmployesProjectsId>,JpaSpecificationExecutor<EmployesProjects>{

}
