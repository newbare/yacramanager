package fr.wati.yacramanager.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Employe;

public interface EmployeRepository extends JpaRepository<Employe, Long> ,JpaSpecificationExecutor<Employe>{

	Employe findByUsername(String username);

	Page<Employe> findByLastNameLike(String string, Pageable pageRequest);

	Page<Employe> findByFirstNameLike(String string, Pageable pageRequest);
}
