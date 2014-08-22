package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.ValidationStatus;

public class CraDetailsDTO {

	private DateTime startDate;
	private DateTime endDate;
	private List<EmployeCraDetailsDTO> employeCraDetailsDTOs=new ArrayList<>();

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

	public List<EmployeCraDetailsDTO> getEmployeCraDetailsDTOs() {
		return employeCraDetailsDTOs;
	}

	public void setEmployeCraDetailsDTOs(
			List<EmployeCraDetailsDTO> employeCraDetailsDTOs) {
		this.employeCraDetailsDTOs = employeCraDetailsDTOs;
	}

	public static class EmployeCraDetailsDTO {
		private Long employeId;
		private String employeName;
		private List<CraDetailDay> days = new ArrayList<>();
		private List<CraTaskRow> taskRows = new ArrayList<>();
		private CraAbsenceDetail craAbsenceDetail;

		public Long getEmployeId() {
			return employeId;
		}

		public void setEmployeId(Long employeId) {
			this.employeId = employeId;
		}

		public String getEmployeName() {
			return employeName;
		}

		public void setEmployeName(String employeName) {
			this.employeName = employeName;
		}

		public List<CraDetailDay> getDays() {
			return days;
		}

		public void setDays(List<CraDetailDay> days) {
			this.days = days;
		}

		public List<CraTaskRow> getTaskRows() {
			return taskRows;
		}

		public void setTaskRows(List<CraTaskRow> taskRows) {
			this.taskRows = taskRows;
		}

		public CraAbsenceDetail getCraAbsenceDetail() {
			return craAbsenceDetail;
		}

		public void setCraAbsenceDetail(CraAbsenceDetail craAbsenceDetail) {
			this.craAbsenceDetail = craAbsenceDetail;
		}
	}

	public static class CraDetailDay{
		private DateTime date;
		private boolean dayOff;
		
		
		
		public CraDetailDay(DateTime date, boolean dayOff) {
			super();
			this.date = date;
			this.dayOff = dayOff;
		}
		public DateTime getDate() {
			return date;
		}
		public void setDate(DateTime date) {
			this.date = date;
		}
		public boolean isDayOff() {
			return dayOff;
		}
		public void setDayOff(boolean dayOff) {
			this.dayOff = dayOff;
		}
		
	}
	
	public static class CraTaskRow {
		private ProjectDTO project;
		private TaskDTO task;
		private Map<DateTime, Long> duration = new HashMap<>();
		private Map<DateTime, ValidationStatus> validationStatus =new HashMap<>();

		public ProjectDTO getProject() {
			return project;
		}

		public void setProject(ProjectDTO project) {
			this.project = project;
		}

		public TaskDTO getTask() {
			return task;
		}

		public void setTask(TaskDTO task) {
			this.task = task;
		}

		public Map<DateTime, Long> getDuration() {
			return duration;
		}

		public void setDuration(Map<DateTime, Long> duration) {
			this.duration = duration;
		}

		public Map<DateTime, ValidationStatus> getValidationStatus() {
			return validationStatus;
		}

		public void setValidationStatus(Map<DateTime, ValidationStatus> validationStatus) {
			this.validationStatus = validationStatus;
		}

	}

	public static class CraAbsenceDetail {
		private Map<DateTime, Long> duration = new HashMap<>();

		public Map<DateTime, Long> getDuration() {
			return duration;
		}

		public void setDuration(Map<DateTime, Long> duration) {
			this.duration = duration;
		}

	}
}
