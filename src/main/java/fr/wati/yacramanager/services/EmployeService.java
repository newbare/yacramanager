package fr.wati.yacramanager.services;

import java.util.List;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.web.dto.RegistrationDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

public interface EmployeService extends CrudService<Employe, Long>,SpecificationFactory<Employe> {

	UserInfoDTO toUserInfoDTO(Long idPersonne) throws Exception;
	
	Employe registerEmploye(RegistrationDTO registrationDTO);

	List<Employe> getManagedEmployees(Long requesterId);
	
	void addManagedEmploye(Long managerId,Long employeId);
}
