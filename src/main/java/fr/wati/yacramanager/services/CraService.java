package fr.wati.yacramanager.services;

import java.util.Date;

import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.web.dto.CraDTO;

public interface CraService {
	
	CraDTO generateCra(Personne personne,Date startDate,Date endDate);

}
