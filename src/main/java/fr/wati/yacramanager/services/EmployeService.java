package fr.wati.yacramanager.services;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.web.dto.RegistrationDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

public interface EmployeService extends CrudService<Employe, Long> {

	UserInfoDTO toUserInfoDTO(Long idPersonne) throws Exception;
	
	Employe registerEmploye(RegistrationDTO registrationDTO); 
}
