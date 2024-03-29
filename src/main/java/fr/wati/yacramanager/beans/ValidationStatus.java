package fr.wati.yacramanager.beans;

import java.util.List;

public enum ValidationStatus {

	SAVED,PENDING, APPROVED, REJECTED;

	public static ValidationStatus isApprovedAndOperator(ValidationStatus status1,
			ValidationStatus status2) {
		if ((PENDING.equals(status1) && APPROVED.equals(status2))
				|| (PENDING.equals(status2) && APPROVED
						.equals(status1))) {
			return PENDING;
		}
		if ((PENDING.equals(status1) && REJECTED.equals(status2))
				|| (PENDING.equals(status2) && REJECTED
						.equals(status1))) {
			return PENDING;
		}
		if ((PENDING.equals(status1) && PENDING.equals(status2))
				|| (PENDING.equals(status2) && PENDING
						.equals(status1))) {
			return PENDING;
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
	public static ValidationStatus multipleIsApprouvedAndOperator(List<ValidationStatus> status){
		ValidationStatus validationStatusToReturn=status.get(0);
		for (int i=1;i <status.size();i++) {
			validationStatusToReturn=isApprovedAndOperator(validationStatusToReturn, status.get(i));
		}
		return validationStatusToReturn;
	}
}
