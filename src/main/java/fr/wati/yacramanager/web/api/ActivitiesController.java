package fr.wati.yacramanager.web.api;

import javax.inject.Inject;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import fr.wati.yacramanager.services.ActivityService;
import fr.wati.yacramanager.web.dto.ActivitiesDetailsDTO;

@RestController
@RequestMapping("/app/api/activities")
public class ActivitiesController {

	@Inject
	private ActivityService activityService;
	
	public ActivitiesController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value="/user/{id}", method = RequestMethod.GET)
	@Timed
	public ActivitiesDetailsDTO forUser(@PathVariable("id") Long userId){
		Pageable pageable=new PageRequest(0, 100);
		return activityService.fromActivities(activityService.findForUser(userId, pageable));
	}
	
	@RequestMapping(value="/company/{id}", method = RequestMethod.GET)
	@Timed
	public ActivitiesDetailsDTO forCompany(@PathVariable("id") Long companyId){
		return null;
	}
	
	@RequestMapping(value="/project/{id}", method = RequestMethod.GET)
	@Timed
	public ActivitiesDetailsDTO forProject(@PathVariable("id") Long projectId){
		return null;
	}
	
	@RequestMapping(value="/client/{id}", method = RequestMethod.GET)
	@Timed
	public ActivitiesDetailsDTO forClient(@PathVariable("id") Long clientId){
		return null;
	}
}
