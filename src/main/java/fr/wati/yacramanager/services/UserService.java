package fr.wati.yacramanager.services;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Users;

public interface UserService extends CrudService<Users, Long>,SpecificationFactory<Employe> {

}
