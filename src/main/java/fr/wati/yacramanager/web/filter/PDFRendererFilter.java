package fr.wati.yacramanager.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PDFRendererFilter implements Filter {

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        // Turn on logging
        System.getProperties().setProperty("xr.util-logging.loggingEnabled", "true");
        //XRLog.setLoggingEnabled(true);
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String outputType = request.getParameter("outputType");

        if (outputType == null || !outputType.equalsIgnoreCase("pdf")) {
            filterChain.doFilter(request, response);
        } else {
//            // Capture the content for this request
//            ContentCaptureServletResponse capContent = new ContentCaptureServletResponse(response);
//            filterChain.doFilter(request, capContent);
//            // Transform the XHTML content to a document readable by the renderer.
//            StringReader contentReader = new StringReader(capContent.getContent());
//            InputSource source = new InputSource(contentReader);
//            try {
////                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
////                documentBuilder.setEntityResolver(FSEntityResolver.instance());
//                //Document xhtmlContent = documentBuilder.parse(source);
//            	response.setContentType("application/pdf");
//            	Document xhtmlContent = XMLResource.load(source).getDocument();
//                ITextRenderer renderer = new ITextRenderer();
//                //renderer.setDocument(xhtmlContent, "");
//                renderer.setDocument(xhtmlContent, null);
//                renderer.layout();
//               
//
//                String filename = String.valueOf(request.getAttribute("filename"));
//                if (filename != null) {
//                    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
//                }
//
//                OutputStream browserStream = response.getOutputStream();
//                renderer.createPDF(browserStream);
//                return;
//            } catch (Exception e) {
//                throw new IOException(e);
//            }
        }
    }

}
