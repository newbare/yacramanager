/**
 * 
 */
package fr.wati.yacramanager.web.api;

import java.util.List;

import fr.wati.yacramanager.web.dto.ResponseWrapper;

/**
 * @author Rachid Ouattara
 *
 */
public interface ApprovalRestService<T> {

	ResponseWrapper<List<T>> getApproval(Long requesterId);
}
