/**
 * 
 */
package fr.wati.yacramanager.web.app.invoices;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import fr.wati.yacramanager.beans.InvoiceItem;
import fr.wati.yacramanager.web.dto.InvoiceDTO;

/**
 * @author Rachid Ouattara
 *
 */
@Component
public class InvoiceExcelView extends AbstractExcelView {

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.document.AbstractExcelView#buildExcelDocument(java.util.Map, org.apache.poi.hssf.usermodel.HSSFWorkbook, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		HSSFSheet excelSheet = workbook.createSheet("Invoice items");
		setExcelHeader(excelSheet);
		
		List<InvoiceItem> invoiceItems = ((InvoiceDTO)model.get("invoice")).getInvoiceItems();
		setExcelRows(excelSheet,invoiceItems);
		
	}

	public void setExcelHeader(HSSFSheet excelSheet) {
		HSSFRow excelHeader = excelSheet.createRow(0);
		excelHeader.createCell(0).setCellValue("Label");
		excelHeader.createCell(1).setCellValue("Description");
//		excelHeader.createCell(2).setCellValue("Type");
//		excelHeader.createCell(3).setCellValue("Aggressive");
//		excelHeader.createCell(4).setCellValue("Weight");
	}
	
	public void setExcelRows(HSSFSheet excelSheet, List<InvoiceItem> invoiceItems){
		int record = 1;
		for (InvoiceItem item : invoiceItems) {
			HSSFRow excelRow = excelSheet.createRow(record++);
			excelRow.createCell(0).setCellValue(item.getItemLabel());
			excelRow.createCell(1).setCellValue(item.getItemDescription());
//			excelRow.createCell(2).setCellValue(animal.getAnimalType());
//			excelRow.createCell(3).setCellValue(animal.getAggressive());
//			excelRow.createCell(4).setCellValue(animal.getWeight());
		}
	}

}
