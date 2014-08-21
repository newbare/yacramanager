package fr.wati.yacramanager.beans;

public interface Valideable {

	ValidationStatus getValidationStatus();
	
	void setValidationStatus(ValidationStatus validationStatus);
}
