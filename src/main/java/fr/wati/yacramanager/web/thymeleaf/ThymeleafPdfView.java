package fr.wati.yacramanager.web.thymeleaf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.spring4.view.ThymeleafView;

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
		super.render(model, request, response);
		System.out.println("Do before");
	}


}
