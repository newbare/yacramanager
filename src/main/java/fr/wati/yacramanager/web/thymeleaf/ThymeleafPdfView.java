package fr.wati.yacramanager.web.thymeleaf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.thymeleaf.spring4.view.ThymeleafView;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.FSEntityResolver;
import org.xml.sax.InputSource;

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
        StringReader contentReader = new StringReader(capContent.getContent());
        InputSource source = new InputSource(contentReader);
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            documentBuilder.setEntityResolver(FSEntityResolver.instance());
            Document xhtmlContent = documentBuilder.parse(source);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(xhtmlContent, "");
//            renderer.setDocument(xhtmlContent, request.getRequestURL().toString());
            renderer.layout();
            response.setContentType("application/pdf");

            String filename = request.getParameter("filename");
            if (filename != null) {
                response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
            }

            OutputStream browserStream = response.getOutputStream();
            renderer.createPDF(browserStream);
        } catch (Exception e) {
            throw new IOException(e);
        }
	}


}
