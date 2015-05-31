/**
 * 
 */
package fr.wati.yacramanager.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Invoice;

/**
 * @author Rachid Ouattara
 *
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long>,JpaSpecificationExecutor<Invoice>{

	Page<Invoice> findByClient_Id(Long clientId,Specification<Invoice> specification,Pageable pageable);
}
