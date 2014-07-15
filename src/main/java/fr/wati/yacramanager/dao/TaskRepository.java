package fr.wati.yacramanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	Task findByProjectAndId(Project project, Long taskId);

}
