/**
 * 
 */
package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.beans.Employe;

/**
 * @author Rachid Ouattara
 *
 */
public interface AttachementRepository extends JpaRepository<Attachement, Long> {

	/**
	 * @param ids
	 * @return
	 */
	List<Attachement> findByIdIn(Long[] ids);
	
	List<Attachement> findByNoteDeFrais_Employe(Employe employe);

}
