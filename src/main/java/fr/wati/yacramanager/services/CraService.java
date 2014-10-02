package fr.wati.yacramanager.services;

import org.joda.time.LocalDate;
import org.springframework.context.ApplicationEventPublisherAware;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDetailsDTO;

public interface CraService extends ApplicationEventPublisherAware {
	
	CraDTO generateCra(Employe employe,LocalDate startDate,LocalDate endDate);

	CraDetailsDTO generateCraDetail(Iterable<Employe> employes,LocalDate startDate,LocalDate endDate) throws ServiceException;
	
	void approve(Iterable<Employe> employes,LocalDate startDate,LocalDate endDate);
}
