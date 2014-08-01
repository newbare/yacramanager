package fr.wati.yacramanager.web.dto;

import java.util.Date;

import fr.wati.yacramanager.beans.WorkLog;

public class WorkLogDTO {

	private long id;
	private String title;
	private Date start;
	private Date end;
	private long employeId;
	private long taskId;
	private String taskName;
	private long duration;
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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
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

	public long getEmployeId() {
		return employeId;
	}

	public void setEmployeId(long employeId) {
		this.employeId = employeId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	
	
}
