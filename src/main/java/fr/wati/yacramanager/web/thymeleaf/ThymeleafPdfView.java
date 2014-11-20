package fr.wati.yacramanager.web.thymeleaf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.thymeleaf.spring4.view.ThymeleafView;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

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
        	String filename = String.valueOf(request.getAttribute("filename"));
          if (request.getAttribute("filename") != null) {
              response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
          }
            OutputStream outputStream = capContent.getOutputStream();
            Document document = new Document(PageSize.LETTER.rotate());
            PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();
            
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);

			htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

			// CSS
			CSSResolver cssResolver = new StyleAttrCSSResolver();
			InputStream csspathtest = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("styles/bootstrap.min.css");
			CssFile bootstrapCssFile = XMLWorkerHelper.getCSS(csspathtest);
			cssResolver.addCss(bootstrapCssFile);
			
			csspathtest = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("styles/font-awesome.min.css");
			CssFile fontAwesomeCssFile = XMLWorkerHelper.getCSS(csspathtest);
			cssResolver.addCss(fontAwesomeCssFile);
			
			csspathtest = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("styles/app.css");
			CssFile appCssFile = XMLWorkerHelper.getCSS(csspathtest);
			cssResolver.addCss(appCssFile);

			Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
					new HtmlPipeline(htmlContext, new PdfWriterPipeline(
							document, pdfWriter)));

			XMLWorker worker = new XMLWorker(pipeline, true);
			XMLParser p = new XMLParser(worker);
			p.parse(IOUtils.toInputStream(content));
            
            //XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document,IOUtils.toInputStream(content)); 
            document.close();
            outputStream.close();

        } catch (Exception e) {
            throw new IOException(e);
        }
	}

}
