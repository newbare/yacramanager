package fr.wati.yacramanager.web.dto;

import fr.wati.yacramanager.beans.Employe;

@SuppressWarnings("serial")
public class UserInfoDTO extends Employe {

	
	
	public static class ManagedEmployeInfoDTO{
		private Long id;
		private String name;
		private String label;
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
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}
		
		
	}
}
