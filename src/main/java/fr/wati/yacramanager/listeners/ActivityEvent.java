package fr.wati.yacramanager.listeners;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationEvent;

import fr.wati.yacramanager.beans.Activities;
import fr.wati.yacramanager.beans.Activities.ActivityOperation;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.utils.SecurityUtils;

@SuppressWarnings("serial")
public class ActivityEvent<DTO> extends ApplicationEvent {

	private Long id;
	private DateTime date;
	private Users user;
	private ActivityOperation activityOperation;
	private Long userId;
	private Long entityId;
	private String entityType;
	private DTO dto;
	
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

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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

	/**
	 * @return the dto
	 */
	public DTO getDto() {
		return dto;
	}

	/**
	 * @param dto the dto to set
	 */
	public void setDto(DTO dto) {
		this.dto = dto;
	}

	public Activities toActivities(){
		Activities activities = new Activities();
		activities.setUser(getUser());
		activities.setActivityOperation(getActivityOperation());
		activities.setEntityId(getEntityId());
		activities.setEntityType(getEntityType());
		activities.setDate(getDate());
		return activities;
	}
	
	public static <DTO> ActivityEvent<DTO> createWithSource(Object source){
		ActivityEvent<DTO> activityEvent = new ActivityEvent<DTO>(source);
		activityEvent.setDate(new DateTime());
		return activityEvent;
	}
	
	public static <DTO> ActivityEvent<DTO> create(){
		ActivityEvent<DTO> activityEvent = new ActivityEvent<DTO>(null);
		activityEvent.setDate(new DateTime());
		return activityEvent;
	}
	
	public ActivityEvent<DTO> user(Users users){
		setUser(users);
		setUserId(users.getId());
		return this;
	}
	
	public ActivityEvent<DTO> user(){
		Employe connectedUser = SecurityUtils.getConnectedUser();
		setUser(connectedUser);
		if(connectedUser!=null){
			setUserId(connectedUser.getId());
		}
		return this;
	}
	
	public ActivityEvent<DTO> operation(ActivityOperation activityOperation){
		setActivityOperation(activityOperation);;
		return this;
	}
	
	public ActivityEvent<DTO> dto(DTO dto){
		setDto(dto);
		return this;
	}
	
	public ActivityEvent<DTO> onEntity(Class<?> entityClass,Long entityId){
		setEntityId(entityId);
		setEntityType(entityClass.getSimpleName());
		return this;
	}
	
}
