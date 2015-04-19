package fr.wati.yacramanager.listeners;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import fr.wati.yacramanager.dao.repository.ActivityRepository;

@Component
public class ActivitiesListener implements ApplicationListener<ActivityEvent>{

	@Inject
	private ActivityRepository activityRepository;
	
	public ActivitiesListener() {
	}

	@Override
	public void onApplicationEvent(ActivityEvent event) {
		activityRepository.save(event.toActivities());
	}

}
