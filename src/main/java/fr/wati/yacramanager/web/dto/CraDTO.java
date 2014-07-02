package fr.wati.yacramanager.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

public class CraDTO {
	
	@JsonDeserialize(using=DateDeserializer.class)
	@JsonSerialize(using=DateSerializer.class)
	private Date startDate;
	@JsonDeserialize(using=DateDeserializer.class)
	@JsonSerialize(using=DateSerializer.class)
	private Date endDate;
	private List<Day> days;

	
	
	
	
	public CraDTO() {
		days=new ArrayList<>();
	}

	public CraDTO(Date startDate, Date endDate) {
		this();
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
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
		private Date date;
		private DayElement morning;
		private DayElement afternoon;
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
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		
	}
	public static class DayElement {
		private boolean worked;

		public boolean isWorked() {
			return worked;
		}

		public void setWorked(boolean worked) {
			this.worked = worked;
		}
		
		
	}
}
