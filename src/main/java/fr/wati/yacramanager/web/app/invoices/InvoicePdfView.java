/**
 * 
 */
package fr.wati.yacramanager.web.app.invoices;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;

import fr.wati.yacramanager.beans.InvoiceItem;
import fr.wati.yacramanager.web.dto.InvoiceDTO;
import fr.wati.yacramanager.web.views.AbstractPdfView;

/**
 * @author Rachid Ouattara
 * 
 */
public class InvoicePdfView extends AbstractPdfView {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.wati.yacramanager.web.views.AbstractPdfView#buildPdfDocument(java.
	 * util.Map, com.itextpdf.text.Document, com.itextpdf.text.pdf.PdfWriter,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void buildPdfDocument(Map<String, Object> model,
			Document document, PdfWriter writer, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Get data "articles" from model
		@SuppressWarnings("unchecked")
		InvoiceDTO invoiceDTO = ((InvoiceDTO) model.get("invoice"));
		List<InvoiceItem> invoiceItems = invoiceDTO.getInvoiceItems();

		// Fonts
		Font fontTitle = new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD,
				BaseColor.BLACK);
		Font fontTag = new Font(FontFamily.HELVETICA, 10, Font.BOLD,
				BaseColor.WHITE);

		for (InvoiceItem item : invoiceItems) {

			// 1.Label
			document.add(new Chunk("Label: "));
			Chunk title = new Chunk(item.getItemLabel(), fontTitle);
			document.add(title);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

			// 1.Description
			document.add(new Chunk("Description: "));
			Chunk description = new Chunk(item.getItemDescription(), fontTitle);
			document.add(description);
			document.add(new Chunk(" "));

			// -- newline
			document.add(Chunk.NEWLINE);

		}
	}

}
