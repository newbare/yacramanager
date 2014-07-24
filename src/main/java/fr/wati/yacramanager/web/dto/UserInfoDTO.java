package fr.wati.yacramanager.web.dto;

import fr.wati.yacramanager.beans.Employe;

@SuppressWarnings("serial")
public class UserInfoDTO extends Employe {

	
	private Navigation navigation;

	public Navigation getNavigation() {
		return navigation;
	}

	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}
	
	public static class ManagedEmployeInfoDTO{
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
		
		
	}
}
