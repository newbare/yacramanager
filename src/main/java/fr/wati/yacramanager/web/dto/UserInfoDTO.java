package fr.wati.yacramanager.web.dto;

import fr.wati.yacramanager.beans.Personne;

@SuppressWarnings("serial")
public class UserInfoDTO extends Personne {

	
	private Navigation navigation;

	public Navigation getNavigation() {
		return navigation;
	}

	public void setNavigation(Navigation navigation) {
		this.navigation = navigation;
	}
	
	
}
