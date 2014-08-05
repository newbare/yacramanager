package fr.wati.yacramanager.web.dto;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.WorkLog;

public class WorkLogDTO {

	private long id;
	private String title;
	private DateTime start;
	private DateTime end;
	private Long employeId;
	private Long taskId;
	private String taskName;
	private Long duration;
	private String description;
	private boolean editable=true;
	private boolean allDay;
	
	public WorkLog toWorkLog(WorkLog workLog) {
		workLog.setDescription(getDescription());
		workLog.setStartDate(getStart());
		workLog.setEndDate(getEnd());
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

	
	
}