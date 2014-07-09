App.factory("AbsenceCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/absences/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});
App.factory("AbsenceTypeREST", function($resource) {
	return $resource(_contextPath+"/app/api/absences/types");
});