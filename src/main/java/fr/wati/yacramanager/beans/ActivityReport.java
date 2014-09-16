package fr.wati.yacramanager.beans;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivityReport extends AuditableEntity implements Valideable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime startDate;
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime endDate;
	@OneToOne
	private Employe employe;
	@Enumerated(EnumType.STRING)
	private ValidationStatus validationStatus=ValidationStatus.SAVED;

	public ActivityReport() {
		// TODO Auto-generated constructor stub
	}

	
	
	public Employe getEmploye() {
		return employe;
	}



	public void setEmploye(Employe employe) {
		this.employe = employe;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
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



	@Override
	public ValidationStatus getValidationStatus() {
		return validationStatus;
	}

	@Override
	public void setValidationStatus(ValidationStatus validationStatus) {
		this.validationStatus=validationStatus;
	}

}
