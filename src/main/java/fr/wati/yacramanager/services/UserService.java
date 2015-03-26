package fr.wati.yacramanager.services;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Users;

public interface UserService extends CrudService<Users, Long>,SpecificationFactory<Employe> {

	void changePassword(Long userId,String password);
	Users activateRegistration(String key);
	void sendActivationMail(Long userId) throws ServiceException;
	Users findByUsername(String username);
}
