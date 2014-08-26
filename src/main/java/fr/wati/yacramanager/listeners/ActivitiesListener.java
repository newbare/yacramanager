package fr.wati.yacramanager.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import fr.wati.yacramanager.dao.repository.ActivityRepository;

@Component
public class ActivitiesListener implements ApplicationListener<ActivityEvent>{

	@Autowired
	private ActivityRepository activityRepository;
	
	public ActivitiesListener() {
	}

	@Override
	public void onApplicationEvent(ActivityEvent event) {
		activityRepository.save(event.toActivities());
	}

}
