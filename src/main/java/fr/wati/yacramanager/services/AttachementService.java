/**
 * 
 */
package fr.wati.yacramanager.services;

import java.util.List;

import fr.wati.yacramanager.beans.Attachement;

/**
 * @author Rachid Ouattara
 *
 */
public interface AttachementService {

	Long addAttachement(Attachement attachement);
	
	List<Attachement> findAttachementsByIds(Long... ids);
	
	void removeAttachement(Attachement attachement);
	
	void update(Attachement attachement);
	
	byte[] getAttachementContent(Long id);
}
