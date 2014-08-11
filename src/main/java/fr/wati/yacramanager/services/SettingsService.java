package fr.wati.yacramanager.services;

import java.util.List;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Settings;
import fr.wati.yacramanager.beans.Users;

public interface SettingsService extends CrudService<Settings, Long> {

	List<Settings> findByUser(Users users);

	List<Settings> findByCompany(Company company);
	
	void addSetting(Users users,Settings settings);
	
	void addSetting(Company company,Settings settings);
}
