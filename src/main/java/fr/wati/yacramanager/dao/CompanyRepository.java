package fr.wati.yacramanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
