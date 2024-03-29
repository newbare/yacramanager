package fr.wati.yacramanager.web.dto;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.beans.Valideable;

public class AbsenceDTO implements Valideable{

	private Long id;
	private Long employeId;
	private String employeName;
	private DateTime date;
	private String description;
	private LocalDate startDate;
	private LocalDate endDate;
	private String typeAbsence;
	private boolean startAfternoon;
	private boolean endMorning;
	private ValidationStatus validationStatus;
	
	public Absence toAbsence(Absence absence){
		absence.setDescription(getDescription());
		absence.setEndDate(getEndDate());
		absence.setStartDate(getStartDate());
		absence.setStartAfternoon(isStartAfternoon());
		absence.setEndMorning(isEndMorning());
		absence.setDate(getDate());
		absence.setTypeAbsence(TypeAbsence.valueOf(getTypeAbsence()));
		absence.setValidationStatus(getValidationStatus());
		return absence;
	}
	
	public static AbsenceDTO fromAbsence(Absence absence){
		AbsenceDTO absenceDTO=new AbsenceDTO();
		absenceDTO.setDescription(absence.getDescription());
		absenceDTO.setStartDate(absence.getStartDate());
		absenceDTO.setEmployeId(absence.getEmploye().getId());
		absenceDTO.setEmployeName(absence.getEmploye().getFullName());
		absenceDTO.setEndDate(absence.getEndDate());
		absenceDTO.setStartAfternoon(absence.isStartAfternoon());
		absenceDTO.setEndMorning(absence.isEndMorning());
		absenceDTO.setValidationStatus(absence.getValidationStatus());
		absenceDTO.setTypeAbsence(absence.getTypeAbsence().name());
		absenceDTO.setDate(absence.getDate());
		absenceDTO.setId(absence.getId());
		return absenceDTO;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Absence toAbsence(){
		return toAbsence(new Absence());
	}
	
	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getTypeAbsence() {
		return typeAbsence;
	}
	public void setTypeAbsence(String typeAbsence) {
		this.typeAbsence = typeAbsence;
	}
	public boolean isStartAfternoon() {
		return startAfternoon;
	}
	public void setStartAfternoon(boolean startAfternoon) {
		this.startAfternoon = startAfternoon;
	}
	
	/**
	 * @return the employeId
	 */
	public Long getEmployeId() {
		return employeId;
	}

	/**
	 * @param employeId the employeId to set
	 */
	public void setEmployeId(Long employeId) {
		this.employeId = employeId;
	}

	/**
	 * @return the employeName
	 */
	public String getEmployeName() {
		return employeName;
	}

	/**
	 * @param employeName the employeName to set
	 */
	public void setEmployeName(String employeName) {
		this.employeName = employeName;
	}

	public boolean isEndMorning() {
		return endMorning;
	}
	public void setEndMorning(boolean endMorning) {
		this.endMorning = endMorning;
	}
	public static class TypeAbsenceDTO{
		private String name;
		private String label;
		
		
		public TypeAbsenceDTO(String name, String label) {
			super();
			this.name = name;
			this.label = label;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public static TypeAbsenceDTO fromEnum(TypeAbsence typeAbsence){
			return new TypeAbsenceDTO(typeAbsence.name(), typeAbsence.getLabel());
		}
	}
	public static enum TypeAbsence{
		CP("Congé payé"),RTT("RTT"),ARRET_MALADIE("Arret maladie"),CONGE_SANS_SOLDE("Congé sans solde"),CONGE_PAR_ANTICIPATIOn("Congé par anticipation");
		private String label;
		
		private TypeAbsence(String label){
			this.label=label;
		}

		public String getLabel() {
			return label;
		}
		
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
