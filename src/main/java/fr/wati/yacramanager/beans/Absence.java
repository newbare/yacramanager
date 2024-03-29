package fr.wati.yacramanager.beans;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import fr.wati.yacramanager.utils.DateUtils;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("serial")
public class Absence extends AuditableEntity implements Valideable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime date;
	private boolean endMorning;
	private boolean startAfternoon;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate startDate;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate endDate;
	private String description;
	@ManyToOne
	private Employe employe;
	@Enumerated(EnumType.STRING)
	private TypeAbsence typeAbsence;
	@Enumerated(EnumType.STRING)
	private ValidationStatus validationStatus;

	public Absence() {
		super();
	}

	public Absence(boolean endMorning, boolean startAfternoon,
			LocalDate startDate, LocalDate endDate, String description) {
		super();
		this.endMorning = endMorning;
		this.startAfternoon = startAfternoon;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public boolean isEndMorning() {
		return endMorning;
	}

	public void setEndMorning(boolean endMorning) {
		this.endMorning = endMorning;
	}

	public boolean isStartAfternoon() {
		return startAfternoon;
	}

	public void setStartAfternoon(boolean startAfternoon) {
		this.startAfternoon = startAfternoon;
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

	public void setEndDate(LocalDate date2) {
		this.endDate = date2;
	}

	/**
	 * @return the employe
	 */
	public Employe getEmploye() {
		return employe;
	}

	/**
	 * @param employe
	 *            the employe to set
	 */
	public void setEmploye(Employe employe) {
		this.employe = employe;
	}

	public TypeAbsence getTypeAbsence() {
		return typeAbsence;
	}

	public void setTypeAbsence(TypeAbsence typeAbsence) {
		this.typeAbsence = typeAbsence;
	}

	@Override
	public ValidationStatus getValidationStatus() {
		return validationStatus;
	}

	@Override
	public void setValidationStatus(ValidationStatus validationStatus) {
		this.validationStatus = validationStatus;
	}

	@Transient
	public double getDaysBetween() {
		return Integer.valueOf(
				DateUtils.getBusinessDaysBetween(getStartDate(), getEndDate()))
				.doubleValue()
				- (isStartAfternoon() ? 0.5 : 0.0)
				- (isEndMorning() ? 0.5 : 0.0);
	}
}
