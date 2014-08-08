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

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("serial")
public class Absence extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime date;
	private boolean endMorning;
	private boolean startAfternoon;
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime startDate;
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime endDate;
	private String description;
	private boolean validated;
	@ManyToOne
	private Employe employe;
	@Enumerated(EnumType.STRING)
	private TypeAbsence typeAbsence;
	
	
	
	
	public Absence() {
		super();
	}
	
	public Absence(boolean endMorning, boolean startAfternoon, DateTime startDate,
			DateTime endDate, String description) {
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
	public boolean isValidated() {
		return validated;
	}
	public void setValidated(boolean validated) {
		this.validated = validated;
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

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime date2) {
		this.endDate = date2;
	}

	

	/**
	 * @return the employe
	 */
	public Employe getEmploye() {
		return employe;
	}

	/**
	 * @param employe the employe to set
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
	
	
}
