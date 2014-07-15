package fr.wati.yacramanager.services;

import fr.wati.yacramanager.beans.Task;

public interface TaskService extends CrudService<Task, Long> {

	Task createTask(Long projectId, Task task);

	void deleteTask(Long projectId, Long taskId);
}
