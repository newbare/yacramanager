package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Activities;

public interface ActivitiesRepository extends JpaRepository<Activities, Long>, JpaSpecificationExecutor<Activities>{

	List<Activities> findByUser_Id(Long userId,Pageable pageable);
	
	List<Activities> findByEntityTypeAndEntityId(String entityType,Long entityId,Pageable pageable);
}
