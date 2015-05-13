package fr.wati.yacramanager.beans;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@IdClass(ActivityReportPK.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityReport extends AuditableEntity implements Valideable {

	@Id
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate startDate;
	@Id
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate endDate;
	@Id
	private Long employeId;
	@Enumerated(EnumType.STRING)
	private ValidationStatus validationStatus=ValidationStatus.SAVED;

	public ActivityReport() {
	}

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



	@Override
	public ValidationStatus getValidationStatus() {
		return validationStatus;
	}

	@Override
	public void setValidationStatus(ValidationStatus validationStatus) {
		this.validationStatus=validationStatus;
	}



	public Long getEmployeId() {
		return employeId;
	}



	public void setEmployeId(Long employeId) {
		this.employeId = employeId;
	}

	@Override
	public String toString() {
		return String
				.format("ActivityReport [employeId=%s, startDate=%s, endDate=%s, validationStatus=%s]",
						employeId, startDate, endDate, validationStatus);
	} 
	
	
}
