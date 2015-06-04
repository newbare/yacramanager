/**
 * 
 */
package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.Invoice;
import fr.wati.yacramanager.beans.InvoiceItem;
import fr.wati.yacramanager.dao.repository.InvoiceRepository;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.InvoiceService;
import fr.wati.yacramanager.utils.Filter;
import fr.wati.yacramanager.web.dto.InvoiceDTO;

/**
 * @author Rachid Ouattara
 *
 */
@Service("invoiceService")
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

	@Inject
	private InvoiceRepository invoiceRepository;
	@Inject
	private ClientService clientService;
	private ApplicationEventPublisher applicationEventPublisher;
	
	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#save(java.lang.Object)
	 */
	@Override
	public <S extends Invoice> S save(S entity) {
		return invoiceRepository.save(entity);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#save(java.lang.Iterable)
	 */
	@Override
	public <S extends Invoice> Iterable<S> save(Iterable<S> entities) {
		return invoiceRepository.save(entities);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findOne(java.io.Serializable)
	 */
	@Override
	public Invoice findOne(Long id) {
		return invoiceRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#exists(java.io.Serializable)
	 */
	@Override
	public boolean exists(Long id) {
		return invoiceRepository.exists(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll()
	 */
	@Override
	public Iterable<Invoice> findAll() {
		return invoiceRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll(java.lang.Iterable)
	 */
	@Override
	public Iterable<Invoice> findAll(Iterable<Long> ids) {
		return invoiceRepository.findAll(ids);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#count()
	 */
	@Override
	public long count() {
		return invoiceRepository.count();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Long id) {
		invoiceRepository.delete(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.lang.Object)
	 */
	@Override
	public void delete(Invoice entity) {
		invoiceRepository.delete(entity);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.lang.Iterable)
	 */
	@Override
	public void delete(Iterable<? extends Invoice> entities) {
		invoiceRepository.delete(entities);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#deleteAll()
	 */
	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException("Cannot delete all");
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll(org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Invoice> findAll(Specification<Invoice> spec, Pageable pageable) {
		return invoiceRepository.findAll(spec, pageable);
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.SpecificationFactory#buildSpecification(fr.wati.yacramanager.utils.Filter)
	 */
	@Override
	public Specification<Invoice> buildSpecification(Filter filter) {
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.InvoiceService#findByClient_Id(java.lang.Long, org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Invoice> findByClient_Id(Long clientId,
			Specification<Invoice> specification, Pageable pageable) {
		return invoiceRepository.findByClient_Id(clientId, specification, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.InvoiceService#toInvoiceDTOs(java.lang.Iterable)
	 */
	@Override
	@Transactional
	public List<InvoiceDTO> toInvoiceDTOs(Iterable<Invoice> invoices) {
		List<InvoiceDTO> dtos=Lists.newArrayList();
		for (Invoice invoice : invoices) {
			dtos.add(toInvoiceDTO(invoice));
		}
		return dtos;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.InvoiceService#toInvoiceDTO(fr.wati.yacramanager.beans.Invoice)
	 */
	@Override
	@Transactional
	public InvoiceDTO toInvoiceDTO(Invoice invoiceInput) {
		Invoice invoice=findOne(invoiceInput.getId());
		InvoiceDTO dto=new InvoiceDTO();
		dto.setId(invoice.getId());
		dto.setInvoiceDate(invoice.getInvoiceDate());
		dto.setDueDate(invoice.getDueDate());
		dto.setSubTotal(invoice.getSubTotal());
		dto.setTotal(invoice.getTotal());
		dto.setTaxes(invoice.getTaxes());
		dto.setInvoiceStatus(invoice.getInvoiceStatus());
		dto.setClient(clientService.toClientDTO(invoice.getClient()));
		dto.setInvoiceNumber(invoice.getInvoiceNumber());
		for(InvoiceItem invoiceItem: invoice.getInvoiceItems()){
			dto.getInvoiceItems().add(invoiceItem);
		}
		return dto;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.InvoiceService#fromDTO(fr.wati.yacramanager.web.dto.InvoiceDTO)
	 */
	@Override
	@Transactional
	public Invoice fromDTO(Invoice input,InvoiceDTO dto) {
		Invoice invoice=findOne(dto.getId());
		invoice.setDueDate(dto.getDueDate());
		invoice.setInvoiceDate(dto.getInvoiceDate());
		invoice.setInvoiceNumber(dto.getInvoiceNumber());
		invoice.setInvoiceStatus(dto.getInvoiceStatus());
		invoice.setTotal(dto.getTotal());
		invoice.setTaxes(dto.getTaxes());
		invoice.setSubTotal(dto.getSubTotal());
		for(InvoiceItem invoiceItem: dto.getInvoiceItems()){
			if(invoice.getInvoiceItems().contains(invoiceItem)){
				//update
				InvoiceItem invoiceItemToUpdate = invoice.getInvoiceItems().get(invoice.getInvoiceItems().indexOf(invoiceItem));
				invoiceItemToUpdate.setItemLabel(invoiceItem.getItemLabel());
				invoiceItemToUpdate.setItemDescription(invoiceItem.getItemDescription());
				invoiceItemToUpdate.setQuantity(invoiceItem.getQuantity());
				invoiceItemToUpdate.setUnitPrice(invoiceItem.getUnitPrice());
			}else {
				//insert
				invoiceItem.setInvoice(invoice);
				invoice.getInvoiceItems().add(invoiceItem);
				
			}
		}
		List<Integer> indexToDelete=new ArrayList<>();
		for(InvoiceItem invoiceItem: invoice.getInvoiceItems()){
			if(!dto.getInvoiceItems().contains(invoiceItem)){
				//delete
				indexToDelete.add(invoice.getInvoiceItems().indexOf(invoiceItem));
			}
		}
		if(!indexToDelete.isEmpty()){
			for(Integer index:indexToDelete){
				invoice.getInvoiceItems().get(index).setInvoice(null);
				invoice.getInvoiceItems().remove(invoice.getInvoiceItems().get(index));
			}
		}
		return invoice;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.InvoiceService#findByInvoiceNumber(java.lang.String)
	 */
	@Override
	public Invoice findByInvoiceNumber(String invoiceNumber) {
		return invoiceRepository.findByInvoiceNumber(invoiceNumber);
	}
}
