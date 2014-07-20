package fr.wati.yacramanager.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>,JpaSpecificationExecutor<Company> {

}
