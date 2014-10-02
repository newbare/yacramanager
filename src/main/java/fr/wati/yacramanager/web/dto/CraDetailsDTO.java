package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;

import fr.wati.yacramanager.beans.ActivityReport;
import fr.wati.yacramanager.beans.ValidationStatus;

public class CraDetailsDTO {

	private LocalDate startDate;
	private LocalDate endDate;
	private List<EmployeCraDetailsDTO> employeCraDetailsDTOs=new ArrayList<>();

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
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
		private List<CraTaskRow> extraTimeRows = new ArrayList<>();
		private CraAbsenceDetail craAbsenceDetail;
		private ActivityReport activityReport;

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

		public List<CraTaskRow> getExtraTimeRows() {
			return extraTimeRows;
		}

		public void setExtraTimeRows(List<CraTaskRow> extraTimeRows) {
			this.extraTimeRows = extraTimeRows;
		}

		public ActivityReport getActivityReport() {
			return activityReport;
		}

		public void setActivityReport(ActivityReport activityReport) {
			this.activityReport = activityReport;
		}
		
		
	}

	public static class CraDetailDay{
		private LocalDate date;
		private boolean dayOff;
		private boolean approved;
		
		
		
		public CraDetailDay(LocalDate date, boolean dayOff,boolean approved) {
			super();
			this.date = date;
			this.dayOff = dayOff;
			this.approved=approved;
		}
		public LocalDate getDate() {
			return date;
		}
		
		
		public boolean isApproved() {
			return approved;
		}
		public void setApproved(boolean approved) {
			this.approved = approved;
		}
		public void setDate(LocalDate date) {
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
		private boolean extraTime;
		private Map<LocalDate, Long> duration = new HashMap<>();
		private Map<LocalDate, ValidationStatus> validationStatus =new HashMap<>();

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

		public Map<LocalDate, Long> getDuration() {
			return duration;
		}

		public void setDuration(Map<LocalDate, Long> duration) {
			this.duration = duration;
		}

		public Map<LocalDate, ValidationStatus> getValidationStatus() {
			return validationStatus;
		}

		public void setValidationStatus(Map<LocalDate, ValidationStatus> validationStatus) {
			this.validationStatus = validationStatus;
		}

		public boolean isExtraTime() {
			return extraTime;
		}

		public void setExtraTime(boolean extraTime) {
			this.extraTime = extraTime;
		}

		
	}

	public static class CraAbsenceDetail {
		private Map<LocalDate, Long> duration = new HashMap<>();
		private Map<LocalDate, ValidationStatus> validationStatus =new HashMap<>();

		public Map<LocalDate, Long> getDuration() {
			return duration;
		}

		public void setDuration(Map<LocalDate, Long> duration) {
			this.duration = duration;
		}

		/**
		 * @return the validationStatus
		 */
		public Map<LocalDate, ValidationStatus> getValidationStatus() {
			return validationStatus;
		}

		/**
		 * @param validationStatus the validationStatus to set
		 */
		public void setValidationStatus(Map<LocalDate, ValidationStatus> validationStatus) {
			this.validationStatus = validationStatus;
		}

	}
}
