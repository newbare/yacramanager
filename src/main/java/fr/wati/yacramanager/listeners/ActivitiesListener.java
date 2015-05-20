package fr.wati.yacramanager.listeners;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.dao.repository.ActivityRepository;

@Component
public class ActivitiesListener implements ApplicationListener<ActivityEvent>{

	@Inject
	private ActivityRepository activityRepository;
	
	@Inject
    private SimpMessageSendingOperations messagingTemplate;
	
	public ActivitiesListener() {
	}

	@Override
	public void onApplicationEvent(ActivityEvent event) {
		activityRepository.save(event.toActivities());
		if(event.getUser()!=null){
			Long companyId = ((Employe)event.getUser()).getCompany().getId();
			event.setUser(null);
			messagingTemplate.convertAndSend("/topic/company/"+companyId+"/event", event);
		}
	}

}
