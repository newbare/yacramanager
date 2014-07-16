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
	
	
}
