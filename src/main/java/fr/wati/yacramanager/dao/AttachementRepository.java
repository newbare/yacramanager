/**
 * 
 */
package fr.wati.yacramanager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Attachement;

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

}
