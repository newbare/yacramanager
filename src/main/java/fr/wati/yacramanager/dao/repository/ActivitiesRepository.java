package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.wati.yacramanager.beans.Activities;

public interface ActivitiesRepository extends JpaRepository<Activities, Long>,
		JpaSpecificationExecutor<Activities> {

	List<Activities> findByUser_Id(Long userId, Pageable pageable);

	List<Activities> findByEntityTypeAndEntityId(String entityType,
			Long entityId, Pageable pageable);

	@Query(nativeQuery = true, value = "select * from ("
			+ "select act.* from employe emp,company comp,activities act where emp.company_id=comp.id and act.entitytype='Users' and act.entityid=emp.id and comp.id=:companyId "
			+ "union "
			+ "select act.* from task task,employe emp,company comp,activities act,tasks_employees task_emp where emp.company_id=comp.id and act.entitytype='Task' and act.entityid=task.id and emp.id=task_emp.employeid and task_emp.taskid=task.id and comp.id=:companyId "
			+ "union "
			+ "select act.* from project project,employe emp,company comp,activities act,projects_employees project_emp where emp.company_id=comp.id and act.entitytype='Project' and act.entityid=project.id and emp.id=project_emp.employeid and project_emp.projectid=project.id and comp.id=:companyId "
			+ "union "
			+ "select act.*  from client client,company comp,activities act where client.company_id=comp.id and act.entitytype='Client' and act.entityid=client.id and comp.id=:companyId "
			+ "union "
			+ "select act.*  from employe emp,notedefrais note,company comp,activities act where emp.company_id=comp.id and note.employe_id=emp.id and act.entitytype='NoteDeFrais' and act.entityid=note.id and comp.id=:companyId "
			+ "union "
			+ "select act.*  from employe emp,worklog worklog,company comp,activities act where emp.company_id=comp.id and worklog.employe_id=emp.id and act.entitytype='WorkLog' and act.entityid=worklog.id and comp.id=:companyId "
			+ "union "
			+ "select act.*  from employe emp,activityreport report,company comp,activities act where emp.company_id=comp.id and report.employeid=emp.id and act.entitytype='ActivityReport' and  act.user_id=emp.id and comp.id=:companyId) as acts "
			+ "order by date desc")
	List<Activities> findForCompany(@Param("companyId") Long companyId);
}
