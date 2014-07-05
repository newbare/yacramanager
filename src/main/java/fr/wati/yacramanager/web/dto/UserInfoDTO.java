package fr.wati.yacramanager.web.dto;

import java.util.HashMap;

import fr.wati.yacramanager.beans.Personne;

@SuppressWarnings("serial")
public class UserInfoDTO extends Personne {

	private HashMap<String, String> settings=new HashMap<>();
	
	
}
