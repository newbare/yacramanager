package fr.wati.yacramanager.listeners;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationEvent;

import fr.wati.yacramanager.beans.Activities;
import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Users;

@SuppressWarnings("serial")
public class ActivityEvent extends ApplicationEvent {

	private Long id;
	private DateTime date;
	private Users user;
	private ActivityOperation activityOperation;
	private Long entityId;
	private String entityType;
	
	public ActivityEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public ActivityOperation getActivityOperation() {
		return activityOperation;
	}

	public void setActivityOperation(ActivityOperation activityOperation) {
		this.activityOperation = activityOperation;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Activities toActivities(){
		Activities activities = new Activities();
		return activities;
	}
	
}
