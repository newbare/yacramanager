package fr.wati.yacramanager.services;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisherAware;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDetailsDTO;

public interface CraService extends ApplicationEventPublisherAware {
	
	CraDTO generateCra(Employe employe,DateTime startDate,DateTime endDate);

	CraDetailsDTO generateCraDetail(Iterable<Employe> employes,DateTime startDate,DateTime endDate);
	
	void approve(Iterable<Employe> employes,DateTime startDate,DateTime endDate);
}
