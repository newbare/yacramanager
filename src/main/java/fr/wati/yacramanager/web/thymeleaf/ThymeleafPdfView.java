package fr.wati.yacramanager.web.thymeleaf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.spring4.view.ThymeleafView;

import com.lowagie.text.Document;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;

import fr.wati.yacramanager.web.ContentCaptureServletResponse;

public class ThymeleafPdfView extends ThymeleafView {

	public ThymeleafPdfView() {
		setContentType("application/pdf");
	}

	public ThymeleafPdfView(String templateName) {
		super(templateName);
		setContentType("application/pdf");
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ContentCaptureServletResponse capContent = new ContentCaptureServletResponse(response);
		super.render(model, request, capContent);
		// Capture the content for this request
		
        // Transform the XHTML content to a document readable by the renderer.
        String content = capContent.getContent();
        try {
        	response.setContentType("application/pdf");
        	String filename = request.getParameter("filename");
          if (filename != null) {
              response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
          }
            OutputStream outputStream = capContent.getOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(content));
            document.close();
            outputStream.close();

        } catch (Exception e) {
            throw new IOException(e);
        }
	}

}
