package fr.wati.yacramanager.services;

import java.util.Date;

import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.web.dto.CraDTO;

public interface CraService {
	
	CraDTO generateCra(Users users,Date startDate,Date endDate);

}
