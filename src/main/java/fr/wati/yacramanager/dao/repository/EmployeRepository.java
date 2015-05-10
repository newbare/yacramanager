package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Task;

public interface EmployeRepository extends JpaRepository<Employe, Long> ,JpaSpecificationExecutor<Employe>{

	Employe findByEmail(String email);
	
	Employe findByContact_Email(String email);
	
	Page<Employe> findByLastNameLike(String string, Pageable pageRequest);

	Page<Employe> findByFirstNameLike(String string, Pageable pageRequest);
	
	List<Employe> findByTasksIn(Task task);

}
