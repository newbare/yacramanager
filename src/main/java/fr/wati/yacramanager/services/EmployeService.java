package fr.wati.yacramanager.services;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

public interface EmployeService extends CrudService<Employe, Long> {

	public UserInfoDTO toUserInfoDTO(Long idPersonne) throws Exception;
}
