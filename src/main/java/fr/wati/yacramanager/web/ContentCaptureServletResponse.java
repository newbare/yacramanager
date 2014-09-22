package fr.wati.yacramanager.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ContentCaptureServletResponse extends HttpServletResponseWrapper {

    private ByteArrayOutputStream contentBuffer;
    private PrintWriter writer;

    public ContentCaptureServletResponse(HttpServletResponse resp) {
        super(resp);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            contentBuffer = new ByteArrayOutputStream();
            writer = new PrintWriter(contentBuffer);
        }
        return writer;
    }

    public String getContent() {
        writer.flush();
        String xhtmlContent = new String(contentBuffer.toByteArray());
        return xhtmlContent;
    }
}