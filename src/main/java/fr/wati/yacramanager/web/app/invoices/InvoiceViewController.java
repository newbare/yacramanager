/**
 * 
 */
package fr.wati.yacramanager.web.app.invoices;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.wati.yacramanager.beans.Invoice;
import fr.wati.yacramanager.services.InvoiceService;
import fr.wati.yacramanager.web.ResourceNotFoundException;

/**
 * @author Rachid Ouattara
 *
 */
@Controller
@RequestMapping("/app/report/invoice")
public class InvoiceViewController {

	@Inject
	private InvoiceService invoiceService;
	
	@RequestMapping("/{token}")
	public String getInvoice(@PathVariable("token") String token,Model model){
		Invoice invoice = invoiceService.findByInvoiceNumber(token);
		if(invoice!=null){
			model.addAttribute("invoice", invoiceService.toInvoiceDTO(invoice));
			return "report/invoice";
		}
		throw new ResourceNotFoundException("This invoice does not exist");
	}
}
