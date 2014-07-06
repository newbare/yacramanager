package fr.wati.yacramanager.web.dto;

import java.util.Date;

import fr.wati.yacramanager.beans.Absence;

public class AbsenceDTO {

	private Long id;
	private Date postedDate;
	private String description;
	private Date startDate;
	private Date endDate;
	private String type;
	private boolean startAfternoon;
	private boolean endMorning;
	private boolean validated;
	
	public Absence toAbsence(Absence absence){
		absence.setDescription(getDescription());
		absence.setEndDate(getEndDate());
		absence.setStartDate(getStartDate());
		absence.setStartAfternoon(isStartAfternoon());
		absence.setEndMorning(isEndMorning());
		absence.setDate(getPostedDate());
		absence.setTypeAbsence(TypeAbsence.valueOf(getType()));
		absence.setValidated(isValidated());
		return absence;
	}
	
	public static AbsenceDTO fromAbsence(Absence absence){
		AbsenceDTO absenceDTO=new AbsenceDTO();
		absenceDTO.setDescription(absence.getDescription());
		absenceDTO.setStartDate(absence.getStartDate());
		absenceDTO.setEndDate(absence.getEndDate());
		absenceDTO.setStartAfternoon(absence.isStartAfternoon());
		absenceDTO.setEndMorning(absence.isEndMorning());
		absenceDTO.setValidated(absence.isValidated());
		absenceDTO.setType(absence.getTypeAbsence().name());
		absenceDTO.setPostedDate(absence.getDate());
		absenceDTO.setId(absence.getId());
		return absenceDTO;
	}
	
	public Long getId() {
		return id;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Absence toAbsence(){
		return toAbsence(new Absence());
	}
	
	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isStartAfternoon() {
		return startAfternoon;
	}
	public void setStartAfternoon(boolean startAfternoon) {
		this.startAfternoon = startAfternoon;
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
}
