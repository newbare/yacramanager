package fr.wati.yacramanager.services;

import java.util.List;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Valideable;

public interface StatusValidator<T extends Valideable,VALIDATOR extends Employe> {

	void validate(VALIDATOR validator, T valideable) throws ServiceException;
	
	void reject(VALIDATOR validator, T valideable) throws ServiceException;
	
	List<T> getEntitiesToApproved(Long employeId);
}
