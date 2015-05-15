package fr.wati.yacramanager.web.dto;

public class AuditableDTO {

	private Long createdBy;
	public AuditableDTO() {
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

}
