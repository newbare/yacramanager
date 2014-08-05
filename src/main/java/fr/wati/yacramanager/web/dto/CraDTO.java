package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class CraDTO {
	
	private DateTime startDate;
	private DateTime endDate;
	private List<Day> days;

	
	
	
	
	public CraDTO() {
		days=new ArrayList<>();
	}

	public CraDTO(DateTime startDate, DateTime endDate) {
		this();
		this.startDate = startDate;
		this.endDate = endDate;
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
	public List<Day> getDays() {
		return days;
	}
	public void setDays(List<Day> days) {
		this.days = days;
	}
	
	public void addDay(Day day){
		this.days.add(day);
	}
	
	public static class Day {
		private DateTime date;
		private DayElement morning;
		private DayElement afternoon;
		private boolean dayOff=false;
		public DayElement getMorning() {
			return morning;
		}
		public void setMorning(DayElement morning) {
			this.morning = morning;
		}
		public DayElement getAfternoon() {
			return afternoon;
		}
		public void setAfternoon(DayElement afternoon) {
			this.afternoon = afternoon;
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
	public static class DayElement {
		private boolean worked;
		private AbsenceDTO absenceDTO;
		
		

		public AbsenceDTO getAbsenceDTO() {
			return absenceDTO;
		}

		public void setAbsenceDTO(AbsenceDTO absenceDTO) {
			this.absenceDTO = absenceDTO;
		}

		public boolean isWorked() {
			return worked;
		}

		public void setWorked(boolean worked) {
			this.worked = worked;
		}
		
		
	}
}
