package fr.wati.yacramanager.beans;

public enum ValidationStatus {

	WAIT_FOR_APPROVEMENT, APPROVED, REJECTED;

	public static ValidationStatus isApprovedAndOperator(ValidationStatus status1,
			ValidationStatus status2) {
		if ((WAIT_FOR_APPROVEMENT.equals(status1) && APPROVED.equals(status2))
				|| (WAIT_FOR_APPROVEMENT.equals(status2) && APPROVED
						.equals(status1))) {
			return WAIT_FOR_APPROVEMENT;
		}
		if ((WAIT_FOR_APPROVEMENT.equals(status1) && REJECTED.equals(status2))
				|| (WAIT_FOR_APPROVEMENT.equals(status2) && REJECTED
						.equals(status1))) {
			return WAIT_FOR_APPROVEMENT;
		}
		if ((WAIT_FOR_APPROVEMENT.equals(status1) && WAIT_FOR_APPROVEMENT.equals(status2))
				|| (WAIT_FOR_APPROVEMENT.equals(status2) && WAIT_FOR_APPROVEMENT
						.equals(status1))) {
			return WAIT_FOR_APPROVEMENT;
		}
		if ((APPROVED.equals(status1) && APPROVED.equals(status2))
				|| (APPROVED.equals(status2) && APPROVED
						.equals(status1))) {
			return APPROVED;
		}
		if ((REJECTED.equals(status1) && APPROVED.equals(status2))
				|| (REJECTED.equals(status2) && APPROVED
						.equals(status1))) {
			return APPROVED;
		}
		if ((REJECTED.equals(status1) && REJECTED.equals(status2))
				|| (REJECTED.equals(status2) && REJECTED
						.equals(status1))) {
			return REJECTED;
		}
		return status1;
	}
}
