package fr.wati.yacramanager.services;

import java.util.Date;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.web.dto.CraDTO;

public interface CraService {
	
	CraDTO generateCra(Employe employe,Date startDate,Date endDate);

}
