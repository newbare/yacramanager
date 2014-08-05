package fr.wati.yacramanager.services;

import org.joda.time.DateTime;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.web.dto.CraDTO;

public interface CraService {
	
	CraDTO generateCra(Employe employe,DateTime startDate,DateTime endDate);

}
