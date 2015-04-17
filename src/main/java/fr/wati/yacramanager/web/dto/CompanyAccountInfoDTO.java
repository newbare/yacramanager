package fr.wati.yacramanager.web.dto;

import org.joda.time.LocalDate;

public class CompanyAccountInfoDTO {

	private boolean locked;
	private LocalDate expiredDate;
	
	public CompanyAccountInfoDTO() {
		// TODO Auto-generated constructor stub
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public LocalDate getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDate expiredDate) {
		this.expiredDate = expiredDate;
	}

	
	
}
