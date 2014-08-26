package fr.wati.yacramanager.beans;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@SuppressWarnings("serial")
@Entity
public class Activities implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime date;
	@OneToOne
	private Users user;
	@Enumerated(EnumType.STRING)
	private ActivityOperation activityOperation;
	private Long entityId;
	private String entityType;
	
	
	public Activities() {
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


	public static enum ActivityOperation{
		CREATE,UPDATE,DELETE,VALIDATE,REJECT;
	}
}
