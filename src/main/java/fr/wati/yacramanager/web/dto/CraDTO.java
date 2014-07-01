package fr.wati.yacramanager.web.dto;

import java.util.Date;
import java.util.List;

public class CraDTO {
	
	
	private Date startDate;
	private Date endDate;
	private List<Day> days;

	
	
	
	
	public class Day {
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
		
	}
	public class DayElement {
		
	}
}
