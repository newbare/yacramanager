package fr.wati.yacramanager.services;

import java.util.List;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;

public interface TaskService extends CrudService<Task, Long>,SpecificationFactory<Task> {

	Task createTask(Long projectId, Task task);

	void deleteTask(Long projectId, Long taskId);
	
	void assignEmployeToTask(Long employeId,Long taskId);
	
	List<Task> findByProjectAndAssignedEmployeesIn(Project project, Employe employe);
}
