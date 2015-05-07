package fr.wati.yacramanager.web.dto;

public class ExceptionDto {

	private String errorMessage;
	
	public ExceptionDto() {
	}

	public ExceptionDto(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
