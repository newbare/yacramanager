package fr.wati.yacramanager.web.dto;

public class AbsenceDTO {

	
	
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
