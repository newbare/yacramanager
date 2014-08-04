package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;

public interface TaskRepository extends JpaRepository<Task, Long>,JpaSpecificationExecutor<Task> {

	Task findByProjectAndId(Project project, Long taskId);
	
	List<Task> findByProjectAndEmploye(Project project, Employe employe);
}
