package fr.wati.yacramanager.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.CompanyAccountInfo;

public interface CompanyAccountInfoRepository extends
		JpaRepository<CompanyAccountInfo, Long> {

	CompanyAccountInfo findByCompany(Company company);
}
