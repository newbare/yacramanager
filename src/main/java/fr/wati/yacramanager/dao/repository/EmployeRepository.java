package fr.wati.yacramanager.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Employe;

public interface EmployeRepository extends JpaRepository<Employe, Long> ,JpaSpecificationExecutor<Employe>{

	Employe findByUsername(String username);

	Page<Employe> findByNomLike(String string, Pageable pageRequest);

	Page<Employe> findByPrenomLike(String string, Pageable pageRequest);
}
