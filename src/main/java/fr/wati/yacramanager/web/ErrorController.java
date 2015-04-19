package fr.wati.yacramanager.web;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;

//@Controller
public class ErrorController {

	@RequestMapping("/error")
	public ModelAndView error(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
		int status=httpServletResponse.getStatus();
		ModelAndView modelAndView=null;
		if(status>=400){
			modelAndView=new ModelAndView(String.valueOf(status));
		}else {
			modelAndView=new ModelAndView("error");
		}
		modelAndView.addObject("status", httpServletRequest.getAttribute("javax.servlet.error.status_code"));
		modelAndView.addObject("reason", httpServletRequest.getAttribute("javax.servlet.error.message"));
		// retrieve some useful information from the request
		  Integer statusCode = (Integer) httpServletRequest.getAttribute("javax.servlet.error.status_code");
		  Throwable throwable = (Throwable) httpServletRequest.getAttribute("javax.servlet.error.exception");
		  // String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
		  String exceptionMessage = getExceptionMessage(throwable, statusCode);
		  String requestUri = (String) httpServletRequest.getAttribute("javax.servlet.error.request_uri");
		  if (requestUri == null) {
		   requestUri = "Unknown";
		  }
		  String message = MessageFormat.format("{0} returned for {1} with message {3}",
		   statusCode, requestUri, exceptionMessage
		  );
		  modelAndView.addObject("errorMessage", message);
		return modelAndView;
	}
	
	private String getExceptionMessage(Throwable throwable, Integer statusCode) {
		  if (throwable != null) {
		   return Throwables.getRootCause(throwable).getMessage();
		  }
		  HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
		  return httpStatus.getReasonPhrase();
		 }
}
