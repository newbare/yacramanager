/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Invoice extends AuditableEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String invoiceNumber;
	private BigDecimal subTotal;
	private BigDecimal total;
	private BigDecimal taxes;
	@ManyToOne
	private Client client;
	@Enumerated(EnumType.STRING)
	private InvoiceStatus invoiceStatus;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate invoiceDate;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dueDate;
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy = "invoice")
	private List<InvoiceItem> invoiceItems=new ArrayList<>();
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy = "invoice")
	private List<InvoicePayment> invoicePayments=new ArrayList<>();
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
	public Client getClient() {
		return client;
	}
	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
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
	 * @return the invoicePayments
	 */
	public List<InvoicePayment> getInvoicePayments() {
		return invoicePayments;
	}
	/**
	 * @param invoicePayments the invoicePayments to set
	 */
	public void setInvoicePayments(List<InvoicePayment> invoicePayments) {
		this.invoicePayments = invoicePayments;
	}
	
	
}
