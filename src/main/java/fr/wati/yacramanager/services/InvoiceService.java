/**
 * 
 */
package fr.wati.yacramanager.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import fr.wati.yacramanager.beans.Invoice;
import fr.wati.yacramanager.web.dto.InvoiceDTO;

/**
 * @author Rachid Ouattara
 *
 */
public interface InvoiceService extends CrudService<Invoice, Long>,SpecificationFactory<Invoice> {

	Page<Invoice> findByClient_Id(Long clientId,Specification<Invoice> specification,Pageable pageable);

	/**
	 * @param findBySpecificationAndOrder
	 * @return
	 */
	List<InvoiceDTO> toInvoiceDTOs(Iterable<Invoice> invoices);
	InvoiceDTO toInvoiceDTO(Invoice invoice);
	
	/**
	 * @param invoice
	 * @param dto
	 * @return
	 */
	Invoice fromDTO(Invoice invoice, InvoiceDTO dto);
}
