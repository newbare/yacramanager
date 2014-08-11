package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Settings;
import fr.wati.yacramanager.beans.Users;

public interface SettingsRepository extends JpaRepository<Settings, Long>, JpaSpecificationExecutor<Settings>{

	List<Settings> findSettingsByUser(Users users);
	
	List<Settings> findSettingsByCompany(Company company);
	
}
