App.factory("NoteCRUDREST", function($resource) {
	return $resource(_contextPath+"/app/api/frais/:id" , {}, {update: { method: 'PUT', params: {id: '@id'} }});
});