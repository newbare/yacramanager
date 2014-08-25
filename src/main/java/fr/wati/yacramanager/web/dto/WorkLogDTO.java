package fr.wati.yacramanager.web.dto;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.beans.Valideable;
import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.beans.WorkLogType;

public class WorkLogDTO implements Valideable{

	private long id;
	private String title;
	private DateTime start;
	private DateTime end;
	private Long employeId;
	private Long taskId;
	private String taskName;
	private Long duration;
	private String description;
	private String projectName;
	private String clientName;
	private boolean editable=true;
	private boolean allDay;
	private String type;
	private String color;
	private ValidationStatus validationStatus;
	
	public WorkLog toWorkLog(WorkLog workLog) {
		workLog.setDescription(getDescription());
		workLog.setStartDate(getStart());
		workLog.setValidationStatus(getValidationStatus());
		if(StringUtils.isNotEmpty(getType())){
			if(String.valueOf(WorkLogType.DURATION).equals(getType())){
				workLog.setWorkLogType(WorkLogType.DURATION);
				workLog.setDuration(getDuration());
			}else {
				workLog.setEndDate(getEnd());
				workLog.setWorkLogType(WorkLogType.TIME);
			}
		}
		return workLog;
	}
	
	public WorkLog toWorkLog() {
		return toWorkLog(new WorkLog());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getStart() {
		return start;
	}

	public void setStart(DateTime dateTime) {
		this.start = dateTime;
	}

	public DateTime getEnd() {
		return end;
	}

	
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public Long getEmployeId() {
		return employeId;
	}

	public void setEmployeId(Long employeId) {
		this.employeId = employeId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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
