/**
 * 
 */
package fr.wati.yacramanager.services;

import java.util.List;

import org.springframework.data.repository.query.Param;

import fr.wati.yacramanager.beans.AbsencePortfolio;
import fr.wati.yacramanager.beans.AbsencePortfolio.AbsencePortfolioPK;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

/**
 * @author Rachid Ouattara
 * 
 */
public interface AbsencePortfolioService extends
		CrudService<AbsencePortfolio, AbsencePortfolioPK> {

	AbsencePortfolio findByUserAndType(@Param("userId") Long userId,
			@Param("typeAbsence") TypeAbsence typeAbsence);

	List<AbsencePortfolio> findByUser(@Param("userId") Long userId);
}
