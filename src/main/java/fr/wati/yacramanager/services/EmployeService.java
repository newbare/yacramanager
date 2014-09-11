package fr.wati.yacramanager.services;

import java.util.List;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.web.dto.RegistrationDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

public interface EmployeService extends CrudService<Employe, Long>,SpecificationFactory<Employe> {

	UserInfoDTO toUserInfoDTO(Long idPersonne) throws Exception;
	
	Employe registerEmploye(RegistrationDTO registrationDTO);

	List<Employe> getManagedEmployees(Long requesterId);
	
	Employe findByContact_Email(String email);
	
	String resetPassword(Employe employe);
	
	/**
	 * 
	 * @param requester the manager
	 * @param employeId the employee to test
	 * @return
	 */
	boolean isManager(Long requester,Long employeId);
	
	void addManagedEmploye(Long managerId,Long employeId);
	
	void createNewEmployee(EmployeDto employeDto,Long companyId,Long managerId);
	
}
