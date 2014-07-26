App.factory("EmployeesCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/users/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});

App.factory("ClientsCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/:companyId/client/:id" , {companyId :'@companyId'}, {update: { method: 'PUT', params: {id: '@id'} }});
});

App.factory("ProjectsCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/:companyId/project/:id" , {companyId :'@companyId'}, {update: { method: 'PUT', params: {id: '@id'} }});
});