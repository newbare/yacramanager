App.factory("AbsenceCRUDREST", function($resource) {
	return $resource("rest/absences");
});
App.factory("AbsenceTypeREST", function($resource) {
	return $resource("rest/absences/types");
});