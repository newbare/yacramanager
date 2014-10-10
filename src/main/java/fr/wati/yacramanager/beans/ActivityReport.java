package fr.wati.yacramanager.beans;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
	@OneToOne
	private Employe employe;
	@Enumerated(EnumType.STRING)
	private ValidationStatus validationStatus=ValidationStatus.SAVED;

	public ActivityReport() {
	}

	
	
	public Employe getEmploye() {
		return employe;
	}



	public void setEmploye(Employe employe) {
		this.employe = employe;
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
	
	
}
