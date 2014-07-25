App.factory("EmployeesCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/users/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});