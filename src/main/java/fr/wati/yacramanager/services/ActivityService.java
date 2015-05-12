package fr.wati.yacramanager.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Activities;
import fr.wati.yacramanager.web.dto.ActivitiesDetailsDTO;

public interface ActivityService {

	List<Activities> findForUser(Long userId,Pageable pageable);
	
	List<Activities> findForCompany(Long companyId,Pageable pageable);
	
	List<Activities> findForProject(Long projectId,Pageable pageable);
	
	List<Activities> findForClient(Long clientId,Pageable pageable);
	
	ActivitiesDetailsDTO fromActivities(List<Activities> activities);
}
