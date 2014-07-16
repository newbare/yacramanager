package fr.wati.yacramanager.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("serial")
public class Absence implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date date;
	private boolean endMorning;
	private boolean startAfternoon;
	private Date startDate;
	private Date endDate;
	private String description;
	private boolean validated;
	@ManyToOne
	private Employe employe;
	@Enumerated(EnumType.STRING)
	private TypeAbsence typeAbsence;
	
	
	
	
	public Absence() {
		super();
	}
	
	public Absence(boolean endMorning, boolean startAfternoon, Date startDate,
			Date endDate, String description) {
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

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
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
