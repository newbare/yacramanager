/**
 * 
 */
package fr.wati.yacramanager.web.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import fr.wati.yacramanager.beans.Invoice;
import fr.wati.yacramanager.beans.InvoiceItem;
import fr.wati.yacramanager.beans.InvoiceStatus;

/**
 * @author Rachid Ouattara
 *
 */
public class InvoiceDTO {

	private Long id;
	private String invoiceNumber;
	private BigDecimal subTotal;
	private BigDecimal total;
	private BigDecimal taxes;
	private ClientDTO client;
	private InvoiceStatus invoiceStatus;
	private LocalDate invoiceDate;
	private LocalDate dueDate;
	private List<InvoiceItem> invoiceItems=new ArrayList<>();
	
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}



	/**
	 * @return the invoiceNumber
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}



	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}



	/**
	 * @return the subTotal
	 */
	public BigDecimal getSubTotal() {
		return subTotal;
	}



	/**
	 * @param subTotal the subTotal to set
	 */
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}



	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}



	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}



	/**
	 * @return the taxes
	 */
	public BigDecimal getTaxes() {
		return taxes;
	}



	/**
	 * @param taxes the taxes to set
	 */
	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}



	/**
	 * @return the client
	 */
	public ClientDTO getClient() {
		return client;
	}



	/**
	 * @param client the client to set
	 */
	public void setClient(ClientDTO client) {
		this.client = client;
	}



	/**
	 * @return the invoiceStatus
	 */
	public InvoiceStatus getInvoiceStatus() {
		return invoiceStatus;
	}



	/**
	 * @param invoiceStatus the invoiceStatus to set
	 */
	public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}



	/**
	 * @return the invoiceDate
	 */
	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}



	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}



	/**
	 * @return the dueDate
	 */
	public LocalDate getDueDate() {
		return dueDate;
	}



	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}



	/**
	 * @return the invoiceItems
	 */
	public List<InvoiceItem> getInvoiceItems() {
		return invoiceItems;
	}



	/**
	 * @param invoiceItems the invoiceItems to set
	 */
	public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}



	/**
	 * @param invoice
	 */
	public Invoice toInvoice(Invoice invoice) {
		invoice.setDueDate(getDueDate());
		invoice.setInvoiceDate(getInvoiceDate());
		invoice.setInvoiceNumber(getInvoiceNumber());
		invoice.setInvoiceStatus(getInvoiceStatus());
		invoice.setTotal(getTotal());
		invoice.setTaxes(getTaxes());
		invoice.setSubTotal(getSubTotal());
		for(InvoiceItem invoiceItem: getInvoiceItems()){
			invoiceItem.setInvoice(invoice);
			invoice.getInvoiceItems().add(invoiceItem);
		}
		return invoice;
	}

}
