package fr.wati.yacramanager.services;

import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.web.dto.UserInfoDTO;

public interface PersonService extends CrudService<Personne, Long> {

	public UserInfoDTO toUserInfoDTO(Long idPersonne) throws Exception;
}
