/**
 * 
 */
package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.wati.yacramanager.beans.AbsencePortfolio;
import fr.wati.yacramanager.beans.AbsencePortfolio.AbsencePortfolioPK;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

/**
 * @author Rachid Ouattara
 *
 */
public interface AbsencePortfolioRepository extends JpaRepository<AbsencePortfolio, AbsencePortfolioPK>, JpaSpecificationExecutor<AbsencePortfolio>{

	@Query("select  p from AbsencePortfolio p where p.absencePortfolioPK.userId = :userId and p.absencePortfolioPK.typeAbsence = :typeAbsence")
	AbsencePortfolio findByUserAndType(@Param("userId") Long userId, @Param("typeAbsence") TypeAbsence typeAbsence);
	
	@Query("select p from AbsencePortfolio p where p.absencePortfolioPK.userId = :userId")
	List<AbsencePortfolio> findByUser(@Param("userId") Long userId);
}
