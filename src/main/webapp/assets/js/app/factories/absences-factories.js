App.factory("AbsenceCRUDREST", function($resource) {
	return $resource("rest/absences/:id");
});
App.factory("AbsenceTypeREST", function($resource) {
	return $resource("rest/absences/types");
});