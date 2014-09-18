package fr.wati.yacramanager.web.thymeleaf;

import org.thymeleaf.spring4.view.ThymeleafViewResolver;

public class ThymeleafPdfViewResolver extends ThymeleafViewResolver {

	public ThymeleafPdfViewResolver() {
		setViewClass(ThymeleafPdfView.class);
	}

}
