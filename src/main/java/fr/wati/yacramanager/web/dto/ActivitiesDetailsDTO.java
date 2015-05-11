package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import fr.wati.yacramanager.beans.Activities;
import fr.wati.yacramanager.dao.repository.EmployeDto;

public class ActivitiesDetailsDTO {

	private List<ActivityDay> days=new ArrayList<>();
	
	public ActivitiesDetailsDTO() {
		// TODO Auto-generated constructor stub
	}

	
	public List<ActivityDay> getDays() {
		return days;
	}


	public void setDays(List<ActivityDay> days) {
		this.days = days;
	}


	public static class ActivityDay{
		
		private LocalDate date;
		private List<ActivityItem> activityItems=new ArrayList<>();
		
		public LocalDate getDate() {
			return date;
		}
		public void setDate(LocalDate date) {
			this.date = date;
		}
		public List<ActivityItem> getActivityItems() {
			return activityItems;
		}
		public void setActivityItems(List<ActivityItem> activityItems) {
			this.activityItems = activityItems;
		}
		
	}
	
	public static class ActivityItem{
		private LocalTime time;
		private ActivityDTO activityDTO;
		public LocalTime getTime() {
			return time;
		}
		public void setTime(LocalTime time) {
			this.time = time;
		}
		public ActivityDTO getActivityDTO() {
			return activityDTO;
		}
		public void setActivityDTO(ActivityDTO activityDTO) {
			this.activityDTO = activityDTO;
		}
		
		
	}
	
	@SuppressWarnings("serial")
	public static class ActivityDTO extends Activities{
		
		private EmployeDto employe;

		public EmployeDto getEmploye() {
			return employe;
		}

		public void setEmploye(EmployeDto employe) {
			this.employe = employe;
		}
		
	}
	
}
