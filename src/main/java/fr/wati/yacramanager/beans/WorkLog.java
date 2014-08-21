/**
 * 
 */
package fr.wati.yacramanager.beans;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
public class WorkLog extends AuditableEntity implements Valideable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	private Task task;
	@ManyToOne
	private Employe employe;
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime startDate;
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime endDate;
	/**
	 * the duration in minutes
	 */
	private Long duration;
	private String description;
	@Enumerated(EnumType.ORDINAL)
	private WorkLogType workLogType;
	@Enumerated(EnumType.STRING)
	private ValidationStatus validationStatus;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}
	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}
	/**
	 * @return the employe
	 */
	public Employe getEmploye() {
		return employe;
	}
	/**
	 * @param employe the employe to set
	 */
	public void setEmploye(Employe employe) {
		this.employe = employe;
	}
	public DateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	public DateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the workLogType
	 */
	public WorkLogType getWorkLogType() {
		return workLogType;
	}
	/**
	 * @param workLogType the workLogType to set
	 */
	public void setWorkLogType(WorkLogType workLogType) {
		this.workLogType = workLogType;
	}
	@Override
	public ValidationStatus getValidationStatus() {
		return validationStatus;
	}
	@Override
	public void setValidationStatus(ValidationStatus validationStatus) {
		this.validationStatus=validationStatus;
	}
	
	
	
}
