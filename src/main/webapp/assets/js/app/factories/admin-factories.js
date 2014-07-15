App.factory("CompanyCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/company/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});